package com.masflam.monerochad.command;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;
import com.masflam.monerochad.service.CoinGeckoService;
import com.masflam.monerochad.service.CoinGeckoService.CryptoPrice;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("xmr/price")
public class XmrPriceCommand implements CommandHandler {
	
	@Inject
	public CoinGeckoService cgs;
	
	@Any
	@Inject
	public PriceCommand priceCommand;
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		CryptoPrice cp = cgs.getPrice("monero");
		var builder = new EmbedBuilder()
			.setAuthor("Monero price", null, Chad.MONERO_LOGO_URL)
			.setColor(Chad.ORANGE)
			.setFooter("Powered by CoinGecko", Chad.COIN_GECKO_LOGO_URL);
		priceCommand.attachFields(builder, cp);
		builder.getDescriptionBuilder()
			.append("\nData from <t:").append(cp.retrieved() / 1000L).append(":R>");
		ihook.sendMessageEmbeds(builder.build()).queue();
	}
}
