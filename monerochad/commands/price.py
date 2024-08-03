from datetime import timezone
import discord
from discord import app_commands
from .. import common
from ..config import settings
from ..service import CryptoInfo, CryptoPrice, coingecko

def attach_fields(embed: discord.Embed, cp: CryptoPrice):
	embed.add_field(
		name=f"USD {settings.UP_EMOJI if cp.usd_24h_change > 0 else settings.DOWN_EMOJI} {cp.usd_24h_change:+.1f}%",
		value=f"```{cp.usd}```",
		inline=True
	)
	embed.add_field(
		name=f"EUR {settings.UP_EMOJI if cp.eur_24h_change > 0 else settings.DOWN_EMOJI} {cp.eur_24h_change:+.1f}%",
		value=f"```{cp.eur}```",
		inline=True
	)
	embed.add_field(
		name=f"BTC {settings.UP_EMOJI if cp.btc_24h_change > 0 else settings.DOWN_EMOJI} {cp.btc_24h_change:+.1f}%",
		value=f"```{cp.btc}```",
		inline=True
	)

def response_embed(ci: CryptoInfo) -> discord.Embed:
	cp = coingecko.get_price(ci.id)
	# TODO: fix UTC timestamps
	tstamp = int(cp.retrieved.astimezone(timezone.utc).timestamp())
	embed = discord.Embed(
		title=f"Price of {ci.name}",
		color=common.ORANGE,
		description=(
			f"ID: `{ci.id}`\n" +
			f"Symbol: `{ci.symbol}`\n" +
			f"Data from: <t:{tstamp}:R>"
		)
	)
	embed.set_footer(text="Powered by CoinGecko", icon_url=common.COIN_GECKO_LOGO_URL)
	attach_fields(embed, cp)
	return embed

def register(tree: app_commands.CommandTree):
	@tree.command(name="price", description="Get the price of a cryptocurrency")
	@app_commands.describe(crypto="The CoinGecko ID of the crypto (e.g. monero)")
	async def cmd(interaction: discord.Interaction, crypto: str):
		await interaction.response.defer()
		
		opts = set()
		by_id = coingecko.info_by_id.get(crypto)
		by_symbol = coingecko.info_by_symbol.get(crypto, set())
		if by_id:
			opts.add(by_id)
		opts.update(by_symbol)
		
		if not opts:
			await interaction.followup.send(embed=common.fail_embed("Unknown crypto"))
		elif len(opts) == 1:
			ci = opts.pop()
			await interaction.followup.send(embed=response_embed(ci))
		else:
			class PriceCommandMenu(discord.ui.Select):
				def __init__(self):
					options = [discord.SelectOption(label=f"{ci.name} ({ci.symbol})", value=ci.id) for ci in opts]
					super().__init__(custom_id="price ambig", placeholder="Ambiguous crypto ID/symbol", options=options)
				
				async def callback(self, interaction: discord.Interaction):
					ci = coingecko.info_by_id[self.values[0]]
					await interaction.response.edit_message(embed=response_embed(ci))
			
			class TheView(discord.ui.View):
				def __init__(self):
					super().__init__()
					self.add_item(PriceCommandMenu())
			view = TheView()
			await interaction.followup.send(view=view)
	
