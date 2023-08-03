import discord
from discord import app_commands
from discord.ext import tasks
from .config import settings
from .service import coingecko

guilds = [discord.Object(int(gid)) for gid in settings.GUILD_IDS.split(",")]

class MoneroChadClient(discord.Client):
	def __init__(self, *, intents: discord.Intents):
		super().__init__(intents=intents)
		self.tree = app_commands.CommandTree(self)
	
	async def setup_hook(self):
		if settings.REGISTER_COMMANDS:
			for guild in guilds:
				print(f"Registering commands for guild {guild.id}")
				self.tree.copy_global_to(guild=guild)
				await self.tree.sync(guild=guild)
			print("Done registering commands")
		else:
			print("Skipping registering commands")
		
		print("Starting nickname changing loop")
		self.nick_loop.start()
		print("Nickname changing loop started")
	
	@tasks.loop(seconds=settings.NICK_LOOP_INTERVAL_SECONDS)
	async def nick_loop(self):
		cp = coingecko.get_price("monero")
		nick = f"${cp.usd:.2f} | MoneroChad"
		for guild in self.guilds:
			await guild.me.edit(nick=nick)
