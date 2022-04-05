package com.masflam.monerochad;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public interface CommandHandler {
	default void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {}
	default void handleButton(ButtonInteractionEvent event, InteractionHook ihook, String idData) throws Exception {}
	default void handleSelectMenu(SelectMenuInteractionEvent event, InteractionHook ihook, String idData) throws Exception {}
}
