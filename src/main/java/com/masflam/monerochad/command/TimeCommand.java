package com.masflam.monerochad.command;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;

import io.quarkus.logging.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("time")
public class TimeCommand implements CommandHandler {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("**hh:mm:ss a**, MMMM d, yyyy");
	
	@Inject
	public ObjectMapper mapper;
	
	private Map<String, String> aliases;
	
	@PostConstruct
	public void postConstruct() {
		try (var stream = getClass().getResourceAsStream("/tz-aliases.json")) {
			aliases = mapper.readValue(stream, new TypeReference<Map<String, String>>() {});
		} catch (Throwable t) {
			Log.errorf(t, "Error while reading timezone aliases");
		}
	}
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		String zone = event.getOption("timezone").getAsString();
		LocalDateTime ldt;
		try {
			ldt = LocalDateTime.now(ZoneId.of(zone, aliases));
		} catch (Exception e) {
			ihook.sendMessage("Unrecognized time zone or area").queue();
			return;
		}
		var builder = new EmbedBuilder()
			.setTitle("Time for `" + zone + "`")
			.setColor(Chad.ORANGE)
			.setDescription(ldt.format(FORMATTER));
		ihook.sendMessageEmbeds(builder.build()).queue();
	}
}
