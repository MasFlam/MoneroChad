from dataclasses import dataclass
from datetime import datetime
from lru import LRU
import requests
from urllib.parse import urlencode
from .config import settings

@dataclass
class CryptoPrice:
	retrieved: datetime
	usd: float
	eur: float
	btc: float
	usd_24h_change: float
	eur_24h_change: float
	btc_24h_change: float

@dataclass(unsafe_hash=True)
class CryptoInfo:
	id: str
	symbol: str
	name: str

class CoinGeckoService:
	BASE_URL: str = "https://api.coingecko.com/api/v3"
	
	def __init__(self):
		self.info_by_id: dict[str, CryptoInfo] = dict()
		self.info_by_symbol: dict[str, set[CryptoInfo]] = dict()
		self._price_cache = LRU(settings.PRICE_CACHE_LIMIT)
	
	def refetch_crypto_info(self):
		self.info_by_id.clear()
		self.info_by_symbol.clear()
		url = f"{self.BASE_URL}/coins/list"
		headers = {"accept": "application/json"}
		r = requests.get(url=url, headers=headers)
		r.raise_for_status()
		json = r.json()
		for info in json:
			ci = CryptoInfo(info["id"], info["symbol"], info["name"])
			self.info_by_id[ci.id] = ci
			if ci.symbol not in self.info_by_symbol:
				self.info_by_symbol[ci.symbol] = set()
			self.info_by_symbol[ci.symbol].add(ci)
	
	def get_price(self, cgid: str) -> CryptoPrice:
		cached: CryptoPrice = self._price_cache.get(cgid)
		if cached and datetime.utcnow() - cached.retrieved <= settings.PRICE_CACHE_TTL:
			return cached
		url = f"{self.BASE_URL}/simple/price"
		params = {
			"vs_currencies": "usd,eur,btc",
			"include_24hr_change": "true",
			"ids": cgid
		}
		headers = {"accept": "application/json"}
		r = requests.get(url=url, params=params, headers=headers)
		r.raise_for_status()
		data = r.json()[cgid]
		cached = CryptoPrice(
			datetime.utcnow(),
			float(data["usd"]),
			float(data["eur"]),
			float(data["btc"]),
			float(data["usd_24h_change"]),
			float(data["eur_24h_change"]),
			float(data["btc_24h_change"]),
		)
		self._price_cache[cgid] = cached
		return cached

coingecko = CoinGeckoService()
coingecko.refetch_crypto_info()

@dataclass
class NetworkInfo:
	retrieved: datetime
	block_height: int
	difficulty: int
	cumulative_difficulty: int
	hard_fork_version: int
	tx_count: int
	hash_rate: int
	fee_per_byte: int

@dataclass
class BlockTransaction:
	hash: str
	coinbase: bool
	fee: int
	size: int

@dataclass
class BlockData:
	retrieved: datetime
	height: int
	hash: str
	size: int
	timestamp: int
	transactions: list[BlockTransaction]

@dataclass
class Mixin:
	block_height: int
	public_key: str
	tx_hash: str

@dataclass
class Input:
	key_image: str
	mixins: list[Mixin]

@dataclass
class Output:
	public_key: str

@dataclass
class TransactionData:
	retrieved: datetime
	hash: str
	block_height: int
	confirmations: int
	coinbase: bool
	timestamp: int
	fee: int
	size: int
	rct_type: int
	version: int
	inputs: list[Input]
	outputs: list[Output]

class MoneroChainService:
	BASE_URL: str = "https://xmrchain.net/api"
	
	def __init__(self):
		self._ni_cache = None
	
	def get_network_info(self) -> NetworkInfo:
		cached = self._ni_cache
		if cached and datetime.utcnow() - cached.retrieved >= settings.NETWORK_INFO_CACHE_TTL:
			return cached
		url = f"{self.BASE_URL}/networkinfo"
		headers = {"accept": "application/json"}
		r = requests.get(url=url, headers=headers)
		r.raise_for_status()
		data = r.json()["data"]
		cached = NetworkInfo(
			datetime.utcnow(),
			int(data["height"]),
			int(data["difficulty"]),
			int(data["cumulative_difficulty"]),
			int(data["current_hf_version"]),
			int(data["tx_count"]),
			int(data["hash_rate"]),
			int(data["fee_per_kb"]) # This is actually fee per byte, it's a bug on the explorer side
		)
		self._ni_cache = cached
		return cached
	
	def get_block_data(self, block_hash_or_height: str) -> BlockData:
		blockid = requests.utils.quote(block_hash_or_height, safe="")
		url = f"{self.BASE_URL}/block/{blockid}"
		headers = {"accept": "application/json"}
		r = requests.get(url=url, headers=headers)
		r.raise_for_status()
		data = r.json()["data"]
		txs_data = data["txs"]
		txs = []
		for tx_data in txs_data:
			tx = BlockTransaction(
				tx_data["tx_hash"],
				bool(tx_data["coinbase"]),
				int(tx_data["tx_fee"]),
				int(tx_data["tx_size"])
			)
			txs.append(tx)
		block = BlockData(
			datetime.utcnow(),
			data["block_height"],
			data["hash"],
			data["size"],
			data["timestamp"],
			txs
		)
		return block
	
	def get_transaction_data(self, tx_hash: str) -> TransactionData:
		txid = requests.utils.quote(tx_hash, safe="")
		url = f"{self.BASE_URL}/transaction/{txid}"
		headers = {"accept": "application/json"}
		r = requests.get(url=url, headers=headers)
		r.raise_for_status()
		data = r.json()["data"]
		
		ins_data = data["inputs"]
		ins = []
		if ins_data:
			for in_data in ins_data:
				mixins_data = in_data["mixins"]
				mixins = []
				for mixin_data in mixins_data:
					mixin = Mixin(
						int(mixin_data["block_no"]),
						mixin_data["public_key"],
						mixin_data["tx_hash"]
					)
					mixins.append(mixin)
				txin = Input(
					in_data["key_image"],
					mixins
				)
				ins.append(txin)
		
		outs_data = data["outputs"]
		outs = []
		for out_data in outs_data:
			outs.append(Output(out_data["public_key"]))
		
		tx = TransactionData(
			datetime.utcnow(),
			data["tx_hash"],
			int(data["block_height"]),
			int(data["confirmations"]),
			bool(data["coinbase"]),
			int(data["timestamp"]),
			int(data["tx_fee"]),
			int(data["tx_size"]),
			int(data["rct_type"]),
			int(data["tx_version"]),
			ins,
			outs
		)
		return tx

monerochain = MoneroChainService()
