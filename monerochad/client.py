from defusedxml import ElementTree
import discord
from discord import app_commands
from discord.ext import tasks
import requests
from . import common
from .config import settings
from .service import coingecko

guilds = [discord.Object(int(gid)) for gid in settings.GUILD_IDS.split(",")]
news_channel_ids = [int(x) for x in settings.NEWS_CHANNEL_IDS.split(",")]
debug_admin_ids = [int(x) for x in settings.DEBUG_ADMIN_IDS.split(",")]

class MoneroChadClient(discord.Client):
	def __init__(self, *, intents: discord.Intents):
		super().__init__(intents=intents, activity=discord.Activity(name="XMR", type=discord.ActivityType.watching))
		self.tree = app_commands.CommandTree(self)
		self._feed_last_id: str = None
		self._feed_loop_first_run: bool = True
	
	async def on_ready(self):
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
		try:
			cp = coingecko.get_price("monero")
			nick = f"${cp.usd:.2f} | MoneroChad"
			for guild in self.guilds:
				await guild.me.edit(nick=nick)
		except BaseException as ex:
			print("Exception in the nick loop")
			print(ex)
			for da_id in debug_admin_ids:
				da = await self.fetch_user(da_id)
				await da.send(f"Exception in the news feed loop:\n```\n{ex}\n```")
	
	@tasks.loop(minutes=settings.FEED_LOOP_INTERVAL_MINUTES)
	async def feed_loop(self):
		FEED_URL = "https://www.monero.observer/feed-mini.xml"
		ICON_URL = "https://www.monero.observer/apple-touch-icon.png"
		
		try:
			r = requests.get(url=FEED_URL)
			
			r.raise_for_status()
			data = ElementTree.fromstring(r.content)
			
			if self._feed_loop_first_run:
				self._feed_last_id = data.find("channel/item/link").text
				self._feed_loop_first_run = False
			
			urls = []
			titles = []
			for item_node in data.findall("channel/item"):
				url = item_node.find("link").text
				title = item_node.find("title").text
				urls.append(url)
				titles.append(title)
			
			try:
				last_idx = urls.index(self._feed_last_id)
			except ValueError:
				last_idx = len(urls)
			
			embeds = []
			for i in reversed(range(0, last_idx)):
				url = urls[i]
				title = titles[i]
				
				embed = discord.Embed(
					title=title,
					description=url,
					url=url,
					color=common.ORANGE,
				)
				embed.set_footer(text="Monero Observer", icon_url=ICON_URL)
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
			
			self._feed_last_id = urls[0]
		except BaseException as ex:
			print("Exception in the news feed loop")
			print(ex)
			for da_id in debug_admin_ids:
				da = await self.fetch_user(da_id)
				await da.send(f"Exception in the news feed loop:\n```\n{ex}\n```")
