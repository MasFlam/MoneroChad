package com.masflam.monerochad.command;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;
import com.masflam.monerochad.command.handler.SelectMenuEditHandler;
import com.masflam.monerochad.service.CryptoPriceService;
import com.masflam.monerochad.service.CryptoPriceService.CryptoInfo;
import com.masflam.monerochad.service.CryptoPriceService.CryptoPrice;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

@ApplicationScoped
@CommandPath("price")
public class PriceCommand implements CommandHandler, SelectMenuEditHandler {
	
	@Inject
	public CryptoPriceService cps;
	
	private EmbedBuilder response(CryptoInfo ci) throws Exception {
		CryptoPrice cp = cps.getPrice(ci.id());
		var builder = new EmbedBuilder()
			.setTitle("Price of " + ci.name())
			.setColor(Chad.YELLOW)
			.setFooter("Powered by CoinGecko", Chad.COIN_GECKO_LOGO_URL);
		builder.getDescriptionBuilder()
			.append("```\nUSD ").append("%.20f".formatted(cp.usd()).replaceAll("\\.?0*$", "")).append("\n```")
			.append("```\nEUR ").append("%.20f".formatted(cp.eur()).replaceAll("\\.?0*$", "")).append("\n```")
			.append("```\nBTC ").append("%.20f".formatted(cp.btc()).replaceAll("\\.?0*$", "")).append("\n```\n")
			.append("ID: `").append(ci.id()).append("`\n")
			.append("Symbol: `").append(ci.symbol()).append("`\n")
			.append("Data from: <t:").append(cp.retrieved() / 1000L).append(":R>");
		return builder;
	}
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		String arg = event.getOption("crypto").getAsString().toLowerCase();
		
		Set<CryptoInfo> opts = new HashSet<>();
		CryptoInfo byId = cps.getCryptoInfoById(arg);
		var bySymbol = cps.getCryptoInfoBySymbol(arg);
		if (byId != null) opts.add(byId);
		opts.addAll(bySymbol);
		
		if (opts.isEmpty()) {
			ihook.sendMessage("Unknown crypto").queue();
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
		var ci = cps.getCryptoInfoById(event.getSelectedOptions().get(0).getValue());
		ihook.editOriginalEmbeds(response(ci).build()).queue();
	}
}
