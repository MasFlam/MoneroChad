import discord

ORANGE = discord.Color.from_str("0xff6600")
GREEN = discord.Color.from_str("0x3de500")
RED = discord.Color.from_str("0xd73000")
BLUE = discord.Color.from_str("0x003fba")
YELLOW = discord.Color.from_str("0xede300")

MONERO_LOGO_URL = "https://www.getmonero.org/press-kit/symbols/monero-symbol-on-white-800.png"
COIN_GECKO_LOGO_URL = "https://static.coingecko.com/s/thumbnail-007177f3eca19695592f0b8b0eabbdae282b54154e1be912285c9034ea6cbaf2.png";

XMR_EMOJI: str = "<:xmr:909154208685629461>"
UP_EMOJI: str = "<:green_up:971470873318014976>"
DOWN_EMOJI: str = "<:red_down:971470925985906759>"
AUTHOR_MENTION: str = "<@141628425835118592>"

DONATE_ADDRESS: str = "89oNRKJhbnHEsCy4Nu6qTGaqVJ2q8o9fuQLSWxc6X959AncFWtaHEjiRPBBnyYpknLYXSmf88Fyo4hwZmmpJw3FaHFy43F4"

def fail_embed(desc: str | None = None) -> discord.Embed:
	return discord.Embed(color=RED, title=desc)
