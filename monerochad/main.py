import discord
from . import commands
from .client import MoneroChadClient
from .config import settings

bot = MoneroChadClient(intents=discord.Intents.default())

commands.register_all(bot)

if __name__ == "__main__":
	bot.run(settings.DISCORD_API_TOKEN)
