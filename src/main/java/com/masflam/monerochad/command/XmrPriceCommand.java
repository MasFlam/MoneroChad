package com.masflam.monerochad.command;

import javax.enterprise.context.ApplicationScoped;
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
	
	private static final String UP_EMOTE = "<:green_up:971470873318014976>";
	private static final String DOWN_EMOTE = "<:red_down:971470925985906759>";
	
	@Inject
	public CoinGeckoService cgs;
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		CryptoPrice cp = cgs.getPrice("monero");
		var builder = new EmbedBuilder()
			.setAuthor("Monero price", null, Chad.MONERO_LOGO_URL)
			.setColor(Chad.ORANGE)
			.setFooter("Powered by CoinGecko", Chad.COIN_GECKO_LOGO_URL)
			.addField(
				"USD %s %+.1f%%".formatted(
					cp.usd24hChange() > 0 ? UP_EMOTE : DOWN_EMOTE,
					cp.usd24hChange()
				),
				"```" + cp.usd() + "```",
				true
			)
			.addField(
				"EUR %s %+.1f%%".formatted(
					cp.eur24hChange() > 0 ? UP_EMOTE : DOWN_EMOTE,
					cp.eur24hChange()
				),
				"```" + cp.eur() + "```",
				true
			)
			.addField(
				"BTC %s %+.1f%%".formatted(
					cp.btc24hChange() > 0 ? UP_EMOTE : DOWN_EMOTE,
					cp.btc24hChange()
				),
				"```" + cp.btc() + "```",
				true
			);
		builder.getDescriptionBuilder()
			.append("\nData from <t:").append(cp.retrieved() / 1000L).append(":R>");
		ihook.sendMessageEmbeds(builder.build()).queue();
	}
}
