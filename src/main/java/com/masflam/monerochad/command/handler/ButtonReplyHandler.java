package com.masflam.monerochad.command.handler;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public interface ButtonReplyHandler {
	void handleButton(ButtonInteractionEvent event, InteractionHook ihook, String idData) throws Exception;
}
