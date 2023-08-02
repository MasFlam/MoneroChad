import discord
from discord import app_commands
from .config import settings

guilds = [discord.Object(int(gid)) for gid in settings.GUILD_IDS.split(",")]

class MoneroChadClient(discord.Client):
	def __init__(self, *, intents: discord.Intents):
		super().__init__(intents=intents)
		self.tree = app_commands.CommandTree(self)
		self.tree.sync
	
	async def setup_hook(self):
		print("dupa")
		self.tree.copy_global_to(guild=guild)
		await self.tree.sync(guild=guild)
