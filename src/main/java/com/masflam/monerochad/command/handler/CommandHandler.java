package com.masflam.monerochad.command.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public interface CommandHandler {
	void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception;
}
