import discord
from io import BytesIO
import qrcode
from qrcode.image.pil import PilImage
from discord import app_commands
from .. import common, database

def register(tree: app_commands.CommandTree):
	@tree.command(name="tip", description="Tip a user with monero")
	@app_commands.describe(user="The user to tip")
	async def cmd(interaction: discord.Interaction, user: discord.Member):
		await interaction.response.defer()
		
		xmr_address = await database.get_tipjar(user.id)
		
		if xmr_address is None:
			embed = discord.Embed(
				color=common.ORANGE,
				description=(
					f"{user.mention} does not have a tip jar yet.\n"
					+ "They should set one up with `/tipjar`!"
				),
			)
			
			await interaction.followup.send(embed=embed)
		else:
			uri = f"monero:{xmr_address}"
			
			qrimg: PilImage = qrcode.make(uri)
			
			buf = BytesIO()
			qrimg.save(buf, 'PNG')
			buf.seek(0)
			
			embed = discord.Embed(
				#title=f"{user.mention}'s Tip Jar",
				color=common.ORANGE,
				description=(
					f"# Tip Jar: {user.mention}\n"
					+ "### Scan the QR code to tip them!\n"
					+ f"```{xmr_address}```"
				),
			)
			embed.set_image(url="attachment://qrcode.png")
			#embed.set_author(name=f"{user.mention}'s Tip Jar", icon_url=common.MONERO_LOGO_URL)
			#embed.set_footer(text=f"Scan the QR code to tip them!", icon_url=common.MONERO_LOGO_URL)
			
			await interaction.followup.send(file=discord.File(buf, "qrcode.png"), embed=embed)
