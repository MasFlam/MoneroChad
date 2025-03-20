from discord import app_commands
from ..client import MoneroChadClient

from . import bot, price, tip, tipjar
from . import xmr_block, xmr_chart, xmr_network, xmr_price, xmr_tx

def register_all(client: MoneroChadClient):
	bot.register(client.tree)
	price.register(client.tree)
	tip.register(client.tree)
	tipjar.register(client.tree)
	
	class XmrCommandsGroup(app_commands.Group):
		def __init__(self):
			super().__init__(name="xmr", description="Monero-related commands")
	xmr = XmrCommandsGroup()
	
	xmr_block.register(xmr)
	xmr_chart.register(xmr)
	xmr_network.register(xmr)
	xmr_price.register(xmr)
	xmr_tx.register(xmr)
	
	client.tree.add_command(xmr)
	
