# MoneroChad Discord Bot
MoneroChad is the bot of a Monero Discord server. It provides mainly Monero- and crypto-related
commands.

### Commands
```
===== Monero =====

/xmr price
 || Get current Monero price.

/xmr chart <exchange> <timeframe>
 || Get the last 30 candles of Monero's price chart.

/xmr links
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


===== Untie (math) =====

/untie help
 || Untie help.

/untie calc <expression>
 || Calculate the result of a mathematical expression.


===== Miscellaneous =====

/time
 || Get current time in a given timezone.

/bot
 || Bot info and credits.
```

### Deployment
- Wherever the command `gradle` is used, you can use `./gradlew` instead if you don't have Gradle
  installed, or you have an old version.
- These commands are meant for Linux deployment.
- You need Java 17 or newer

#### Build the uber jar
```sh
gradle build -Dquarkus.package.type=uber-jar
```
Now `build/monerochad-VERSION-runner.jar` is a standalone, runnable jar. You can copy it to wherever
you want to run the bot from.

#### Create a `.env` file
```
MONEROCHAD_TOKEN=Your Discord API bot token
MONEROCHAD_GUILD_IDS=Comma separated IDs of guilds the bot runs in
MONEROCHAD_NEWS_CHANNEL_IDS=Comma separated IDs of channels the bot relays news feeds to
```
Put this into a file named `.env` in the directory the bot will run in.

#### Run the bot
```sh
java -jar monerochad-VERSION-runner.jar
```
