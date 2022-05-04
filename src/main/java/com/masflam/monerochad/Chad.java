package com.masflam.monerochad;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.dom4j.io.SAXReader;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import okhttp3.OkHttpClient;

@ApplicationScoped
public class Chad {
	
	// Our colors
	public static final int ORANGE = 0xff6600;
	public static final int GREEN = 0x3de500;
	public static final int RED = 0xd73000;
	public static final int BLUE = 0x003fba;
	public static final int YELLOW = 0xede300;
	
	public static final String MONERO_LOGO_URL = "https://www.getmonero.org/press-kit/symbols/monero-symbol-on-white-800.png";
	public static final String COIN_GECKO_LOGO_URL = "https://static.coingecko.com/s/thumbnail-007177f3eca19695592f0b8b0eabbdae282b54154e1be912285c9034ea6cbaf2.png";
	
	@ConfigProperty(name = "monerochad.token")
	protected String token;
	
	@ConfigProperty(name = "monerochad.guild-ids")
	protected String guildIds;
	
	@Inject
	public DiscordListener discordListener;
	
	@Produces
	public OkHttpClient httpClient = new OkHttpClient();
	
	@Produces
	public SAXReader saxReader = new SAXReader();
	
	@Produces
	public JDA jda;
	
	@Produces
	public ObjectMapper objectMapper = new ObjectMapper();
	
	public static EmbedBuilder failEmbed() {
		return new EmbedBuilder().setColor(RED);
	}
	
	public static EmbedBuilder failEmbed(String desc) {
		return new EmbedBuilder().setColor(RED).setTitle(desc);
	}
	
	public void onStartup(@Observes StartupEvent evt) throws LoginException, InterruptedException {
		jda = JDABuilder.createLight(token)
			.setHttpClient(httpClient)
			.addEventListeners(discordListener)
			.build().awaitReady();
		
		jda.getPresence().setActivity(Activity.watching("XMR"));
		
		for (String id : guildIds.split(",")) {
			var guild = jda.getGuildById(id);
			
			guild.updateCommands().addCommands(
				Commands.slash("untie", "Math").addSubcommands(
					new SubcommandData("help", "Command help"),
					new SubcommandData("calc", "Calculate a mathematical expression")
						.addOption(OptionType.STRING, "expression", "The expresion to calculate", true)
				),
				Commands.slash("price", "Get the price of a cryptocurrency")
					.addOption(OptionType.STRING, "crypto", "The CoinGecko ID of the crypto (e.g. monero)", true),
				Commands.slash("bot", "Bot info"),
				Commands.slash("time", "Get current time around the world")
					.addOption(OptionType.STRING, "timezone", "Time zone or location, case matters! (e.g. ET, CET, UTC+2, Europe/Brussels, America/New_York)", true),
				Commands.slash("xmr", "Monero-related commands").addSubcommands(
					new SubcommandData("price", "Get Monero price"),
					new SubcommandData("chart", "Get Monero price chart")
						.addOptions(
							new OptionData(OptionType.STRING, "exchange", "The exchange to get data about", true)
								.addChoice("Binance", "binance;Binance;xmrusdt;XMR/USDT")
								.addChoice("Kraken", "kraken;Kraken;xmrusd;XMR/USD")
								.addChoice("KuCoin", "kucoin;KuCoin;xmrusdt;XMR/USDT")
								.addChoice("Okex", "okex;Okex;xmrusdt;XMR/USDT")
								.addChoice("Huobi", "huobi;Huobi;xmrusdt;XMR/USDT")
								.addChoice("Bitfinex", "bitfinex;Bitfinex;xmrusd;XMR/USD")
								.addChoice("Gate.io", "gateio;Gate.io;xmrusdt;XMR/USDT")
								.addChoice("HitBTC", "hitbtc;HitBTC;xmrusdt;XMR/USDT"),
							new OptionData(OptionType.STRING, "interval", "How much time should one candle represent", true)
								.addChoice("minute", "60")
								.addChoice("3 minutes", "180")
								.addChoice("5 minutes", "300")
								.addChoice("15 minutes", "900")
								.addChoice("30 minutes", "1800")
								.addChoice("hour", "3600")
								.addChoice("2 hours", "7200")
								.addChoice("4 hours", "14400")
								.addChoice("6 hours", "21600")
								.addChoice("12 hours", "43200")
								.addChoice("day", "86400")
								.addChoice("3 days", "259200")
								.addChoice("week", "604800")
						),
					new SubcommandData("links", "Get a list of links to Monero resources"),
					new SubcommandData("tx", "Get info on a monero transaction")
						.addOption(OptionType.STRING, "hash", "Transaction hash", true),
					new SubcommandData("network", "Monero network information"),
					new SubcommandData("block", "Get info on a block")
						.addOption(OptionType.STRING, "block", "Blockheight (block number) or block hash", true)
				)
			).queue();
			
		}
	}
}
