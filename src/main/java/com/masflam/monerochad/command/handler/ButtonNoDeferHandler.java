package com.masflam.monerochad.command.handler;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonNoDeferHandler {
	void handleButton(ButtonInteractionEvent event, String idData) throws Exception;
}
