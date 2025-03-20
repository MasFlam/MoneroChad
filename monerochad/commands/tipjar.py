import discord
from io import BytesIO
import qrcode
from qrcode.image.pil import PilImage
from discord import app_commands
from .. import common, database

def register(tree: app_commands.CommandTree):
	@tree.command(name="tipjar", description="Set (or remove) your tip jar's monero address")
	@app_commands.describe(xmr_address="Monero address (omit to remove your current one)")
	async def cmd(interaction: discord.Interaction, xmr_address: str = None):
		await interaction.response.defer()
		user_id = interaction.user.id
		
		if xmr_address is None:
			await database.unset_tipjar(user_id)
			
			embed = discord.Embed(
				color=common.ORANGE,
				description=f"Removed your tip jar.",
			)
			
			await interaction.followup.send(embed=embed)
		elif not common.is_valid_monero_address(xmr_address):
			await interaction.followup.send(embed=common.fail_embed("Invalid monero address"))
		else:
			await database.set_tipjar(user_id, xmr_address)
			
			embed = discord.Embed(
				color=common.ORANGE,
				description=f"Your tip jar has been set up! Others can now tip you with `/tip`.",
			)
			
			await interaction.followup.send(embed=embed)
