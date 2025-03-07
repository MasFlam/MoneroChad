import discord
from discord import app_commands
from time import sleep
from . import price as price_cmd
from .. import common
from ..service import TransactionData, monerochain

def register(group: app_commands.Group):
	@group.command(name="block", description="Get info on a block")
	@app_commands.describe(block_hash_or_height="Block height (block number) or block hash")
	@app_commands.rename(block_hash_or_height="block")
	async def cmd(interaction: discord.Interaction, block_hash_or_height: str):
		await interaction.response.defer()
		
		block = monerochain.get_block_data(block_hash_or_height)
		
		embed = discord.Embed(
			title=f"Monero Block {block.height}",
			color=common.ORANGE,
			description=f"`{block.hash}`"
		)
		embed.add_field(name="Date and Time", value=f"<t:{block.timestamp}>", inline=True)
		embed.add_field(name="Size", value=f"{block.size} B", inline=True)
		embed.add_field(name="Transactions", value=str(len(block.transactions)), inline=True)
		
		class TheView(discord.ui.View):
			def __init__(self):
				super().__init__()
				self.add_item(discord.ui.Button(
					label="View on xmrchain.net",
					url=f"https://xmrchain.net/block/{block.height}"
				))
		view = TheView()
		
		await interaction.followup.send(embed=embed, view=view)
