package com.masflam.monerochad;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
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
	
	@ConfigProperty(name = "monerochad.token", defaultValue = "")
	protected String token;
	
	@Inject
	public DiscordListener discordListener;
	
	@Produces
	public OkHttpClient httpClient;
	
	@Produces
	public ObjectMapper objectMapper = new ObjectMapper();
	
	public void onStartup(@Observes StartupEvent evt) throws LoginException, InterruptedException {
		var jda = JDABuilder.createLight(token)
			.addEventListeners(discordListener)
			.build().awaitReady();
		
		httpClient = jda.getHttpClient();
		
		var guild = jda.getGuildById(959831716464308254L);
		guild.upsertCommand("calc", "Calculate a mathematical expression")
			.addOption(OptionType.STRING, "expression", "The expresion to calculate", true)
			.queue();
		guild.upsertCommand("price", "Get the price of a cryptocurrency")
			.addOption(OptionType.STRING, "crypto", "The CoinGecko ID of the crypto (e.g. monero)")
			.queue();
		guild.upsertCommand("xmr", "Monero-related commands")
			.addSubcommands(
				new SubcommandData("price", "Get Monero price"),
				new SubcommandData("tx", "Get info on a monero transaction")
					.addOption(OptionType.STRING, "hash", "Transaction hash", true),
				new SubcommandData("network", "Monero network information"),
				//new SubcommandData("mempool", "List transactions currently in the mempool"),
				//new SubcommandData("txs", "List transactions")
				//	.addOption(OptionType.INTEGER, "pageno", "Page number", false)
				//	.addOption(OptionType.INTEGER, "pagesz", "Number of transactions per page", false),
				new SubcommandData("block", "Get info on a block")
					.addOption(OptionType.STRING, "block", "Blockheight (block number) or block hash", true),
				new SubcommandData("links", "Get a list of links to Monero resources")
			).queue();
	}
	
	public void onShutdown(@Observes ShutdownEvent evt) {
		System.out.println("Aaaaau");
	}
}
