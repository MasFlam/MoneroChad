import discord
from discord import app_commands
from .. import common
import monerochad

def register(tree: app_commands.CommandTree):
	@tree.command(name="bot", description="Bot info")
	async def cmd(interaction: discord.Interaction):
		embed = discord.Embed(
			title="MoneroChad",
			color=common.ORANGE,
			description=(
				f"Monero-themed Discord bot. {common.XMR_EMOJI}\n" + 
				"Please report any encountered bugs as GitHub issues or directly to the author. :beetle:\n" + 
				"**GitHub:** https://github.com/MasFlam/MoneroChad"
			)
		)
		embed.add_field(name="Author", value=common.AUTHOR_MENTION, inline=True)
		embed.add_field(name="Version", value=monerochad.__version__, inline=True)
		embed.add_field(name="Donations are welcome :)", value=f"```{common.DONATE_ADDRESS}```", inline=False)
		await interaction.response.send_message(embed=embed)
