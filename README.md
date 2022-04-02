# MoneroChad Discord Bot
MoneroChad is the bot of a Monero Discord server. It provides mainly Monero- and crypto-related
commands.

### Commands
```
===== Monero =====

/xmr price
 || Get current Monero price

/xmr tx <tx-hash>
 || Get info about the given Monero transaction.

/xmr block <block-height|block-hash>
 || Get info about the given Monero block.

/xmr network
 || Get info about the Monero network.


===== Miscellaneous =====

/calc <expression>
 || Calculate the result of a mathematical expression.
```

<!--
## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/monerochad-0.1.0-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.
-->
