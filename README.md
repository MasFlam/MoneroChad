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

### Deployment with Docker Compose

There is a basic [`compose.yaml`](docker/compose.yaml) that you can modify to fit your needs.
All the settings configurable through environment variables are listed in [`config.py`](monerochad/config.py).

To start, you can just replace the environment variables with your data, and run the bot using:
```sh
# Inside the ./docker directory:
docker compose up -d

# Inspect logs:
docker compose logs -f

# Shut down:
docker compose down
```
For more information, see the [Docker Compose docs](https://docs.docker.com/compose/).

Note that this compose file mounts `./schema.sql` as a volume to the postgres container, which is
the file executed on the container's first start, to create the `tipjars` table in the database.

