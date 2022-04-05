package com.masflam.monerochad.command;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.CommandHandler;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.Chad;
import com.masflam.monerochad.service.MoneroChainService;
import com.masflam.monerochad.service.MoneroChainService.NetworkInfo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("xmr/network")
public class XmrNetworkCommand implements CommandHandler {
	
	@Inject
	public MoneroChainService mcs;
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		NetworkInfo ni = mcs.getNetworkInfo();
		
		var builder = new EmbedBuilder()
			.setColor(Chad.ORANGE)
			.addField("Blockheight", String.valueOf(ni.blockheight()), true)
			.addField("Difficulty", ni.difficulty(), true)
			.addField("All-time Difficulty", ni.cumulativeDifficulty(), true)
			.addField("Hashrate", "%.1f GH/s".formatted(ni.hashRate() / 1e9), true)
			.addField("Hard Fork", "v" + ni.hardFork(), true)
			.addField("All-time Transactions", String.valueOf(ni.txCount()), true)
			.setFooter("Powered by blox.minexmr.com");
		
		ihook.sendMessageEmbeds(builder.build())
			.queue();
	}
}
