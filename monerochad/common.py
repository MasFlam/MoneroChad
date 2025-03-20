import discord
import re

ORANGE = discord.Color.from_str("0xff6600")
GREEN = discord.Color.from_str("0x3de500")
RED = discord.Color.from_str("0xd73000")
BLUE = discord.Color.from_str("0x003fba")
YELLOW = discord.Color.from_str("0xede300")

MONERO_LOGO_URL = "https://www.getmonero.org/press-kit/symbols/monero-symbol-on-white-800.png"
COIN_GECKO_LOGO_URL = "https://static.coingecko.com/s/thumbnail-007177f3eca19695592f0b8b0eabbdae282b54154e1be912285c9034ea6cbaf2.png";

AUTHOR_MENTION: str = "<@141628425835118592>"

DONATE_ADDRESS: str = "89oNRKJhbnHEsCy4Nu6qTGaqVJ2q8o9fuQLSWxc6X959AncFWtaHEjiRPBBnyYpknLYXSmf88Fyo4hwZmmpJw3FaHFy43F4"

MONERO_ADDRESS_REGEX = re.compile(r"^[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{95}$")

def is_valid_monero_address(xmr_address: str) -> bool:
	return MONERO_ADDRESS_REGEX.fullmatch(xmr_address) is not None

def fail_embed(desc: str | None = None) -> discord.Embed:
	return discord.Embed(color=RED, title=desc)
