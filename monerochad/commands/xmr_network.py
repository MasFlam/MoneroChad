import discord
from discord import app_commands
import logging
from time import sleep
from . import price as price_cmd
from .. import common
from ..service import NetworkInfo, monerochain

logger = logging.getLogger(__name__)

def register(group: app_commands.Group):
	@group.command(name="network", description="Monero network information")
	async def cmd(interaction: discord.Interaction):
		await interaction.response.defer()
		ni: NetworkInfo = None
		
		# /networkinfo likes to bug out (or at least it used to)
		ntries = 0
		while not ni and ntries < 3:
			try:
				ni = monerochain.get_network_info()
			except:
				logger.error("Stupid buggy /networkinfo")
				sleep(0.4)
				ntries += 1
		
		embed = discord.Embed()
		embed.color = common.ORANGE
		embed.add_field(name="Block height", value=str(ni.block_height), inline=True)
		embed.add_field(name="Difficulty", value=str(ni.difficulty), inline=True)
		embed.add_field(name="All-time Difficulty", value=str(ni.cumulative_difficulty), inline=True)
		embed.add_field(name="Hashrate", value=f"{ni.hash_rate/1e9:.2f} GH/s", inline=True)
		embed.add_field(name="Hard Fork", value=f"v{ni.hard_fork_version}", inline=True)
		embed.add_field(name="All-time Transactions", value=str(ni.tx_count), inline=True)
		embed.set_footer(text="Powered by xmrchain.net API")
		await interaction.followup.send(embed=embed)
