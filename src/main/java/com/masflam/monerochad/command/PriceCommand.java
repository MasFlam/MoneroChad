package com.masflam.monerochad.command;

import java.util.concurrent.ExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;
import com.masflam.monerochad.exception.NotFoundException;
import com.masflam.monerochad.service.CryptoPriceService;
import com.masflam.monerochad.service.CryptoPriceService.CryptoPrice;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("price")
public class PriceCommand implements CommandHandler {
	
	@Inject
	public CryptoPriceService cps;
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		String cgid = event.getOption("crypto").getAsString().toLowerCase();
		CryptoPrice cp;
		try {
			cp = cps.getPrice(cgid);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof NotFoundException) {
				ihook.sendMessage("Unknown crypto").queue();
				return;
			} else {
				throw e;
			}
		}
		var builder = new EmbedBuilder()
			.setTitle("Price of " + cgid)
			.setFooter("Powered by CoinGecko", Chad.COIN_GECKO_LOGO_URL);
		builder.getDescriptionBuilder()
			.append("```\nUSD ").append(cp.usd()).append("\n```")
			.append("```\nEUR ").append(cp.eur()).append("\n```")
			.append("```\nBTC ").append(cp.btc()).append("\n```")
			.append("\nData from <t:").append(cp.retrieved() / 1000L).append(":R>");
		ihook.sendMessageEmbeds(builder.build()).queue();
	}
}
