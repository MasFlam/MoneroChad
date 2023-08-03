import discord
from discord import app_commands
from discord.ext import tasks
import requests
from . import common
from .config import settings
from .service import coingecko

guilds = [discord.Object(int(gid)) for gid in settings.GUILD_IDS.split(",")]
news_channel_ids = [int(x) for x in settings.NEWS_CHANNEL_IDS.split(",")]

class MoneroChadClient(discord.Client):
	def __init__(self, *, intents: discord.Intents):
		super().__init__(intents=intents)
		self.tree = app_commands.CommandTree(self)
		self._feed_last_id: str = None
		self._feed_loop_first_run: bool = True
	
	async def setup_hook(self):
		if settings.REGISTER_COMMANDS:
			for guild in guilds:
				print(f"Registering commands for guild {guild.id}")
				self.tree.copy_global_to(guild=guild)
				await self.tree.sync(guild=guild)
			print("Done registering commands")
		else:
			print("Skipping registering commands")
		
		print("Starting loops")
		self.nick_loop.start()
		self.feed_loop.start()
		print("Loops started")
	
	@tasks.loop(seconds=settings.NICK_LOOP_INTERVAL_SECONDS)
	async def nick_loop(self):
		cp = coingecko.get_price("monero")
		nick = f"${cp.usd:.2f} | MoneroChad"
		for guild in self.guilds:
			await guild.me.edit(nick=nick)
	
	@tasks.loop(minutes=settings.FEED_LOOP_INTERVAL_MINUTES)
	async def feed_loop(self):
		FEED_URL = "https://localmonero.co/static/rss/the-monero-standard/feed.json"
		ICON_URL = "https://localmonero.co/static/img/localmonero/meta/icons/apple-touch-icon.png"
		
		r = requests.get(url=FEED_URL)
		
		try:
			r.raise_for_status()
			json = r.json()
		except object as ex:
			print(ex)
			return
		
		if not json["items"]:
			return
		
		if self._feed_loop_first_run:
			self._feed_last_id = json["items"][0]["id"]
			self._feed_loop_first_run = False
		
		if json["items"][0]["id"] == self._feed_last_id:
			return
		
		try:
			last_idx = [item["id"] for item in json["items"]].index(self._feed_last_id)
		except ValueError:
			last_idx = len(json["items"])
		
		embeds = []
		for i in reversed(range(0, last_idx)):
			item = json["items"][i]
			url = item["url"]
			title = item["title"]
			summary = item["summary"]
			
			embed = discord.Embed(
				title=title,
				description=summary,
				url=url,
				color=common.ORANGE
			)
			embed.set_footer(text="The Monero Standard", icon_url=ICON_URL)
			embeds.append(embed)
		
		embeds_grouped = []
		for i in range(len(embeds)):
			if i % 10 == 0:
				embeds_grouped.append([])
			embeds_grouped[-1].append(embeds[i])
		
		for chid in news_channel_ids:
			channel = self.get_channel(chid)
			for grp in embeds_grouped:
				await channel.send(embeds=grp)
		
		self._feed_last_id = json["items"][0]["id"]
