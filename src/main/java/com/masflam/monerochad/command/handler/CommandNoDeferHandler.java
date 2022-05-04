package com.masflam.monerochad.command.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandNoDeferHandler {
	void handle(SlashCommandInteractionEvent event) throws Exception;
}
