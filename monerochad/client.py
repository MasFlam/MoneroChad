from defusedxml import ElementTree
import discord
from discord import app_commands
from discord.ext import tasks
import logging
import requests
from . import common
from .config import settings
from .service import coingecko

logger = logging.getLogger(__name__)

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
			logger.info("Registering commands for guilds")
			for guild in guilds:
				logger.info("Registering commands for guild %d", guild.id)
				self.tree.copy_global_to(guild=guild)
				await self.tree.sync(guild=guild)
			logger.info("Done registering commands")
		else:
			logger.info("Skipping registering commands")
		
		if settings.NICK_LOOP_ENABLED:
			logger.info("Starting nick loop")
			self.nick_loop.start()
			logger.info("Nick loop started")
		else:
			logger.info("Skipping nick loop start, resetting nickname instead")
			for guild in self.guilds:
				logger.info("Resetting nickname in guild %d", guild.id)
				await guild.me.edit(nick="MoneroChad")
			logger.info("Nickname reset done")
		
		if settings.FEED_LOOP_ENABLED:
			logger.info("Starting feed loop")
			self.feed_loop.start()
			logger.info("Feed loop started")
		else:
			logger.info("Skipping feed loop start")

        @tasks.loop(seconds=settings.NICK_LOOP_INTERVAL_SECONDS)
        async def nick_loop(self):
            try:
                # main loop
                cp = coingecko.get_price("monero")
                nick = f"${cp.usd:.2f} | MoneroChad"
                for guild in self.guilds:
                    try:
                        await guild.me.edit(nick=nick)
                    except discord.errors.Forbidden as forbidden_error:
                        logger.error(f"Could not change nickname in guild {guild.id}: {forbidden_error}")
                        # even if it fails, continue with other guilds
            except BaseException as ex:
                logger.error("Exception in the nick loop", exc_info=True)
                for da_id in debug_admin_ids:
                    try:
                        da = await self.fetch_user(da_id)
                        await da.send(f"Exception in the nick loop:\n```\n{ex}\n```")
                    except discord.errors.Forbidden as forbidden_error: # God forbid not send message, god teach not interrupt dragon tiger fight
                        logger.error(f"Could not send exception message to admin {da_id}: {forbidden_error}")
            # loop go on even after error
            finally:
                pass  # life goes on and on and on

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
			logger.error("Exception in the news feed loop", exc_info=True)
			for da_id in debug_admin_ids:
				da = await self.fetch_user(da_id)
				await da.send(f"Exception in the news feed loop:\n```\n{ex}\n```")


