import discord
from discord import app_commands
from time import sleep
from . import price as price_cmd
from .. import common
from ..service import TransactionData, monerochain

def register(group: app_commands.Group):
	@group.command(name="tx", description="Get info on a monero transaction")
	@app_commands.describe(tx_hash="Transaction hash")
	@app_commands.rename(tx_hash="hash")
	async def cmd(interaction: discord.Interaction, tx_hash: str):
		await interaction.response.defer()
		
		tx = monerochain.get_transaction_data(tx_hash)
		
		embed = discord.Embed(
			title="Monero Transaction",
			color=common.ORANGE
		)
		embed.add_field(name="Block height", value=("N/A" if tx.block_height == 0 else str(tx.block_height)), inline=True)
		embed.add_field(name="Fee", value=f"{round(tx.fee/1e6)} µɱ\n({round((tx.fee/1e6) / (tx.size/1000))} per kB)", inline=True)
		embed.add_field(name="Size", value=f"{tx.size} B", inline=True)
		embed.add_field(name="Date and Time", value=f"<t:{tx.timestamp}>", inline=True)
		embed.add_field(name="RingCT Type", value=("N/A" if tx.rct_type == 0 and not tx.coinbase else str(tx.rct_type)), inline=True)
		embed.add_field(name="Transaction Version", value=str(tx.version), inline=True)
		embed.add_field(name="Confirmations", value=str(tx.confirmations), inline=True)
		embed.add_field(name="Inputs", value=str(len(tx.inputs)), inline=True)
		embed.add_field(name="Outputs", value=str(len(tx.outputs)), inline=True)
		embed.set_footer(text="Powered by xmrchain.net API")
		
		desc = f"`{tx.hash}`"
		if tx.coinbase:
			desc += "\nThis is a coinbase (aka miner's reward) transaction."
		embed.description = desc
		
		class TheView(discord.ui.View):
			def __init__(self):
				super().__init__()
				self.add_item(discord.ui.Button(
					label="View on xmrchain.net",
					url=f"https://xmrchain.net/tx/{tx.hash}"
				))
		view = TheView()
		
		await interaction.followup.send(embed=embed, view=view)
