import discord
from discord import app_commands
from io import BytesIO
import mplfinance as mpf
import pandas
import time
from .. import common
from ..service import Candle, kraken

VS_CHOICES = [
	app_commands.Choice(name="USDT", value="XMRUSDT;XMR/USDT"),
	app_commands.Choice(name="BTC", value="XXMRXXBT;XMR/BTC"),
]

INTERVAL_CHOICES = [
	app_commands.Choice(name="1 minute", value="1;1m"),
	app_commands.Choice(name="5 minutes", value="5;5m"),
	app_commands.Choice(name="15 minutes", value="15;15m"),
	app_commands.Choice(name="30 minutes", value="30;30m"),
	app_commands.Choice(name="1 hour", value="60;1h"),
	app_commands.Choice(name="4 hours", value="240;4h"),
	app_commands.Choice(name="1 day", value="1440;1d"),
	app_commands.Choice(name="1 week", value="10080;1w"),
	app_commands.Choice(name="15 days", value="21600;15d"),
]

# Remember to update the command description too when changing the cooldown
COOLDOWN_SECONDS = 15

prevtime = 0.0

def register(group: app_commands.Group):
	
	marketcolors = mpf.make_marketcolors(base_mpf_style="yahoo", wick="inherit")
	mpf_style = mpf.make_mpf_style(base_mpf_style="yahoo", marketcolors=marketcolors)
	
	@group.command(name="chart", description="Get Monero price chart (has 15 second cooldown)")
	@app_commands.describe(vs_val="Symbol of the coin to compare XMR to", interval_val="How much time should one candle represent")
	@app_commands.choices(vs_val=VS_CHOICES, interval_val=INTERVAL_CHOICES)
	@app_commands.rename(vs_val="vs", interval_val="interval")
	async def cmd(interaction: discord.Interaction, vs_val: str, interval_val: str):
		global prevtime
		nowtime = time.time()
		delta_seconds = nowtime - prevtime
		if delta_seconds < COOLDOWN_SECONDS:
			again = COOLDOWN_SECONDS - delta_seconds
			await interaction.response.send_message(
				embed=common.fail_embed(f"The command is on cooldown. Try again in **{again:.1f}** seconds."),
				ephemeral=True
			)
			return
		prevtime = nowtime
		await interaction.response.defer()
		
		pair, pair_display_name = vs_val.split(";")
		interval, interval_display_name = interval_val.split(";")
		
		interval_minutes = int(interval)
		since = (int(time.time()) - 30 * interval_minutes*60)
		
		candles = kraken.get_ohlc(pair, interval, since)
		df = pandas.DataFrame([
			{
				"Date": candle.timestamp,
				"Open": candle.o,
				"High": candle.h,
				"Low": candle.l,
				"Close": candle.c,
				"Volume": candle.volume,
			}
			for candle in candles
		]).set_index("Date")
		
		buf = BytesIO()
		mpf.plot(df, volume=False, savefig=dict(fname=buf, format="png", bbox_inches="tight"), type="candle", style=mpf_style)
		buf.seek(0)
		
		embed = discord.Embed(color=common.ORANGE)
		embed.set_image(url="attachment://chart.png")
		embed.set_footer(text=f"{pair_display_name} ({interval_display_name}) on Kraken")
		
		await interaction.followup.send(file=discord.File(fp=buf, filename="chart.png"), embed=embed)
