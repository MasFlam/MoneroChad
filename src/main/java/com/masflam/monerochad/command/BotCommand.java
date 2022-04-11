package com.masflam.monerochad.command;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;

import io.quarkus.runtime.ApplicationConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("bot")
public class BotCommand implements CommandHandler {
	
	private static final String ADDRESS = "89oNRKJhbnHEsCy4Nu6qTGaqVJ2q8o9fuQLSWxc6X959AncFWtaHEjiRPBBnyYpknLYXSmf88Fyo4hwZmmpJw3FaHFy43F4";
	
	@Inject
	public ApplicationConfig appConfig;
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		var builder = new EmbedBuilder()
			.setTitle("MoneroChad")
			.setColor(Chad.ORANGE)
			.addField("Author", "<@141628425835118592>", true)
			.addField("Version", appConfig.version.get(), true)
			.addField("Donations are welcome :)", "```%s```".formatted(ADDRESS), false)
			.setDescription("Monero-themed Discord bot. <:xmr:909154208685629461>");
		ihook.sendMessageEmbeds(builder.build()).queue();
	}
}
