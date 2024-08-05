import discord
import logging
from . import commands
from .client import MoneroChadClient
from .config import settings

def main():
	logging.basicConfig(
		formatter='{asctime} [{name}/{filename}/{funcName}:{lineno}] {levelname}: {message}',
		datefmt='%Y-%m-%d %H:%M:%S',
		style='{',
	)
	bot = MoneroChadClient(intents=discord.Intents.default())
	commands.register_all(bot)
	bot.run(settings.DISCORD_API_TOKEN, log_handler=None)

if __name__ == "__main__":
	main()
