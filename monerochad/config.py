from datetime import timedelta
from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
	model_config = SettingsConfigDict(
		env_file=".env",
		env_file_encoding="utf-8"
	)
	
	DISCORD_API_TOKEN: str
	GUILD_IDS: str
	REGISTER_COMMANDS: bool = True
	PRICE_CACHE_TTL: timedelta = timedelta(minutes=1)
	PRICE_CACHE_LIMIT: int = 15
	NETWORK_INFO_CACHE_TTL: timedelta = timedelta(minutes=1)
	NICK_LOOP_INTERVAL_SECONDS: float = 20.0

settings = Settings()
