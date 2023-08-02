import discord
from discord import app_commands
from . import price as price_cmd
from .. import common
from ..service import coingecko

def register(group: app_commands.Group):
	@group.command(name="price", description="Get Monero price")
	async def cmd(interaction: discord.Interaction):
		await interaction.response.defer()
		cp = coingecko.get_price("monero")
		embed = discord.Embed()
		embed.set_author(name="Monero price", icon_url=common.MONERO_LOGO_URL)
		embed.color = common.ORANGE
		embed.set_footer(text="Powered by CoinGecko", icon_url=common.COIN_GECKO_LOGO_URL)
		price_cmd.attach_fields(embed, cp)
		await interaction.followup.send(embed=embed)
