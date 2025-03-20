import asyncio
import discord
import logging
from . import commands, database
from .client import MoneroChadClient
from .config import settings

async def main():
	logging.basicConfig(
		format='{asctime} [{name}/{filename}/{funcName}:{lineno}] {levelname}: {message}',
		datefmt='%Y-%m-%d %H:%M:%S',
		style='{',
		level=logging.INFO,
	)
	await database.pool.open(wait=True)
	bot = MoneroChadClient(intents=discord.Intents.default())
	commands.register_all(bot)
	await bot.start(settings.DISCORD_API_TOKEN)

if __name__ == "__main__":
	asyncio.run(main())
