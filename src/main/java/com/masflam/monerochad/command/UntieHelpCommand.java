package com.masflam.monerochad.command;

import javax.enterprise.context.ApplicationScoped;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("untie/help")
public class UntieHelpCommand implements CommandHandler {
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		var builder = new EmbedBuilder()
			.setTitle("`/calc` Help")
			.setColor(Chad.ORANGE)
			.addField("`+`, `-`, `*`, `/`, `^`, `(`, `)`", "You know how they work.", false)
			.addField("`where` expressions", "```2 + foo * bar where foo = 2 + 7 where bar = 4^2```", false)
			.addField("Literals", "Currently only integer literals are supported.", false);
		ihook.sendMessageEmbeds(builder.build()).queue();
	}
}
