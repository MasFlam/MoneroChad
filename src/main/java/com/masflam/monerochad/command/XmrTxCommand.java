package com.masflam.monerochad.command;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.CommandHandler;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.service.MoneroChainService;
import com.masflam.monerochad.service.MoneroChainService.TransactionData;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("xmr/tx")
public class XmrTxCommand implements CommandHandler {
	
	@Inject
	public MoneroChainService mcs;
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		String hash = event.getOption("hash").getAsString();
		TransactionData td = mcs.getTransactionData(hash);
		
		var builder = new EmbedBuilder()
			.setTitle("Transaction `%s...%s`".formatted(
				td.hash.subSequence(0, 4),
				td.hash.subSequence(td.hash.length() - 4, td.hash.length())
			), "https://xmrchain.net/tx/" + td.hash)
			.addField("Blockheight", String.valueOf(td.blockHeight), true)
			.addField("Fee", Math.round(td.fee / 1e6) + " µɱ\n(" + Math.round((td.fee / 1e6) / (td.size / 1000d)) + " per kB)", true)
			.addField("Size", td.size + " B", true)
			.addField("Date and Time", "<t:" + td.timestamp + ">", true)
			.addField("RingCT Type", td.rctType == 0 && !td.coinbase ? "N/A" : String.valueOf(td.rctType), true)
			.addField("Transaction Version", String.valueOf(td.version), true)
			.addField("Confirmations", String.valueOf(td.confirmations), true)
			.addField("Inputs", String.valueOf(td.inputs.length), true)
			.addField("Outputs", String.valueOf(td.outputs.length), true)
			.setFooter("Powered by blox.minexmr.com");
		
		var desc = builder.getDescriptionBuilder();
		
		if (td.coinbase) {
			desc.append("This is a coinbase (aka miner's reward) transaction.");
		}
		
		// TODO: buttons for navigatin inputs/outputs?
		ihook.sendMessageEmbeds(builder.build())
			.queue();
	}
}
