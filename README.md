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

### Docker Deployment

#### Build the image
```sh
docker build -t monerochad:0.2.1 -f docker/Dockerfile .
```

#### Create a `.env` file
You copy and modify the one at [`docker/template.env`](docker/template.env).
All the settings you can configure through `.env` are in [`config.py`](monerochad/config.py).

#### Run the container
```sh
docker run --env-file path/to/your/.env monerochad:0.2.1
```
It doesn't need any volumes or ports exposed.

### Manual Deployment (discouraged)
These instructions are meant for deployment on Linux. You need Python 3.10 or higher.

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
You copy and modify the one at [`docker/template.env`](docker/template.env).
All the settings you can configure through `.env` are in [`config.py`](monerochad/config.py).

#### Run the bot
```sh
# Activate virtual environment
source .venv/bin/activate
# Run the bot
python3 -m monerochad.main
```
