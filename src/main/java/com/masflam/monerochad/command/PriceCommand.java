package com.masflam.monerochad.command;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;
import com.masflam.monerochad.command.handler.SelectMenuEditHandler;
import com.masflam.monerochad.service.CoinGeckoService;
import com.masflam.monerochad.service.CoinGeckoService.CryptoInfo;
import com.masflam.monerochad.service.CoinGeckoService.CryptoPrice;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

@ApplicationScoped
@CommandPath("price")
public class PriceCommand implements CommandHandler, SelectMenuEditHandler {
	
	private static final String UP_EMOTE = "<:green_up:971470873318014976>";
	private static final String DOWN_EMOTE = "<:red_down:971470925985906759>";
	
	@Inject
	public CoinGeckoService cgs;
	
	public void attachFields(EmbedBuilder builder, CryptoPrice cp) {
		builder
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
	}
	
	private EmbedBuilder response(CryptoInfo ci) throws Exception {
		CryptoPrice cp = cgs.getPrice(ci.id());
		var builder = new EmbedBuilder()
			.setTitle("Price of " + ci.name())
			.setColor(Chad.ORANGE)
			.setFooter("Powered by CoinGecko", Chad.COIN_GECKO_LOGO_URL);
		builder.getDescriptionBuilder()
			.append("ID: `").append(ci.id()).append("`\n")
			.append("Symbol: `").append(ci.symbol()).append("`\n")
			.append("Data from: <t:").append(cp.retrieved() / 1000L).append(":R>");
		attachFields(builder, cp);
		return builder;
	}
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		String arg = event.getOption("crypto").getAsString().toLowerCase();
		
		Set<CryptoInfo> opts = new HashSet<>();
		CryptoInfo byId = cgs.getCryptoInfoById(arg);
		var bySymbol = cgs.getCryptoInfoBySymbol(arg);
		if (byId != null) opts.add(byId);
		opts.addAll(bySymbol);
		
		if (opts.isEmpty()) {
			ihook.sendMessageEmbeds(Chad.failEmbed("Unknown crypto").build()).queue();
			return;
		} else if (opts.size() == 1) {
			CryptoInfo ci = null;
			for (var c : opts) ci = c;
			ihook.sendMessageEmbeds(response(ci).build()).queue();
			return;
		}
		
		var menu = SelectMenu.create("price ambig")
			.setPlaceholder("Ambiguous crypto ID/symbol");
		for (var c : opts) {
			menu.addOption("%s (%s)".formatted(c.name(), c.symbol()), c.id());
		}
		ihook.sendMessage("")
			.addActionRow(menu.build())
			.queue();
	}
	
	@Override
	public void handleSelectMenu(SelectMenuInteractionEvent event, InteractionHook ihook, String idData)
			throws Exception {
		var ci = cgs.getCryptoInfoById(event.getSelectedOptions().get(0).getValue());
		ihook.editOriginalEmbeds(response(ci).build()).queue();
	}
}
