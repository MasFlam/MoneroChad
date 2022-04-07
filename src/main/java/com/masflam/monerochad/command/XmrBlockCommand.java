package com.masflam.monerochad.command;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;
import com.masflam.monerochad.service.MoneroChainService;
import com.masflam.monerochad.service.MoneroChainService.BlockData;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

@ApplicationScoped
@CommandPath("xmr/block")
public class XmrBlockCommand implements CommandHandler {
	
	@Inject
	public MoneroChainService mcs;
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		String blockHashOrHeight = event.getOption("block").getAsString();
		BlockData bd = mcs.getBlockData(blockHashOrHeight);
		
		var builder = new EmbedBuilder()
			.setTitle("Monero Block " + bd.height())
			.setColor(Chad.ORANGE)
			.setDescription("`" + bd.hash() + "`")
			.addField("Date and Time", "<t:" + bd.timestamp() + ">", true)
			.addField("Size", bd.size() + " B", true)
			.addField("Transactions", String.valueOf(bd.txs().length), true)
			.setFooter("Powered by blox.minexmr.com");
		
		ihook.sendMessageEmbeds(builder.build())
			.addActionRow(Button.link("https://xmrchain.net/block/" + bd.height(), "View on xmrchain.net"))
			.queue();
	}
}
