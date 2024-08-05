from datetime import timedelta
from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
	model_config = SettingsConfigDict(
		env_file=".env",
		env_file_encoding="utf-8"
	)
	
	DISCORD_API_TOKEN: str
	GUILD_IDS: str
	NEWS_CHANNEL_IDS: str
	DEBUG_ADMIN_IDS: str
	REGISTER_COMMANDS: bool = True
	PRICE_CACHE_TTL: timedelta = timedelta(minutes=1)
	PRICE_CACHE_LIMIT: int = 15
	NETWORK_INFO_CACHE_TTL: timedelta = timedelta(minutes=1)
	NICK_LOOP_ENABLED: bool = True
	FEED_LOOP_ENABLED: bool = True
	NICK_LOOP_INTERVAL_SECONDS: float = 20.0
	FEED_LOOP_INTERVAL_MINUTES: float = 20.0
	
	# You should add the emojis in /assets to your bot app and replace these in your .env.
	UP_EMOJI: str = "<:green_up:1269286678053261436>"
	DOWN_EMOJI: str = "<:red_down:1269286696755925094>"

settings = Settings()
