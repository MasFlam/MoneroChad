# MoneroChad Discord Bot
MoneroChad is a Discord bot that provides Monero and cryptocurrency related commands.
It used to be written in Java and JDA, but has been rewritten to Python and discord.py
for performance (memory consumption) reasons. The old java code sits on the `old-java` branch
of this repository. MoneroChad has been running on one of the Monero-focused Discord servers
since April 2022.

### License

Copyright (C) by Łukasz Drukała

MoneroChad and all the code in this repository is licensed under the GNU Affero General Public License version 3.
(**not** "3 or later"). A copy of the license is provided with the source code in the file named `LICENSE`.

### Commands
```
===== Monero =====

/xmr price
 || Get current Monero price.

/xmr chart <vs> <timeframe>
 || Get the last 30 candles of Monero's price chart.

/xmr links  (Not implemented yet)
 || Browse links to Monero-related sites and resources.

/xmr tx <tx-hash>
 || Get info about the given Monero transaction.

/xmr network
 || Get info about the Monero network.

/xmr block <block-height|block-hash>
 || Get info about the given Monero block.


===== Cryptocurrency =====

/price <coingecko-id>
 || Get the price of the given crypto.


===== Miscellaneous =====

/bot
 || Bot info and credits.
```

### Deployment

The bot uses Python 3.10 features, so you need at least that version.

These instructions are meant for deployment on Linux.

#### Create venv and install dependencies
```sh
# Create virtual environment
python3 -m venv .venv
# Activate virtual environment
source .venv/bin/activate
# Install dependencies
python3 -m pip install -r requirements.txt
```

#### Create a `.env` file
```
DISCORD_API_TOKEN=Your Discord API bot token
GUILD_IDS=Comma separated IDs of guilds the bot runs in
NEWS_CHANNEL_IDS=Comma separated IDs of channels the bot relays news feeds to
```
Put this into a file named `.env` in the directory the bot will run in.

You can see all the settings you can configure through `.env` in [`config.py`](monerochad/config.py).

#### Run the bot
```sh
# Activate virtual environment
source .venv/bin/activate
# Run the bot
python3 -m monerochad.main
```
