package com.masflam.monerochad.command;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;
import com.masflam.monerochad.command.handler.SelectMenuEditHandler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

@ApplicationScoped
@CommandPath("xmr/links")
public class XmrLinksCommand implements CommandHandler, SelectMenuEditHandler {
	
	private static record Section(String title, String[] links) {}
	
	private Section[] sections;
	private SelectMenu.Builder menuBuilder;
	
	@Inject
	public ObjectMapper mapper;
	
	@PostConstruct
	public void init() throws StreamReadException, DatabindException, IOException {
		try (var stream = getClass().getResourceAsStream("/links.json")) {
			sections = mapper.readValue(stream, Section[].class);
		}
		menuBuilder = SelectMenu.create("xmr/links browse").setPlaceholder("Choose topic");
		for (int i = 0; i < sections.length; ++i) {
			menuBuilder.addOption(sections[i].title(), String.valueOf(i));
		}
	}
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		ihook.sendMessage("")
			.addActionRow(menuBuilder.build())
			.queue();
	}
	
	@Override
	public void handleSelectMenu(SelectMenuInteractionEvent event, InteractionHook ihook, String idData)
			throws Exception {
		int idx = Integer.parseInt(event.getValues().get(0));
		var sec = sections[idx];
		var builder = new EmbedBuilder()
			.setColor(Chad.ORANGE)
			.setThumbnail(Chad.MONERO_LOGO_URL)
			.setTitle(sec.title());
		var desc = builder.getDescriptionBuilder();
		for (String link : sec.links) {
			desc.append(link).append('\n');
		}
		ihook.editOriginalEmbeds(builder.build()).queue();
	}
}
