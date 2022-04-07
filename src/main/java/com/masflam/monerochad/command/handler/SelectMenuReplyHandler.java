package com.masflam.monerochad.command.handler;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public interface SelectMenuReplyHandler {
	void handleSelectMenu(SelectMenuInteractionEvent event, InteractionHook ihook, String idData) throws Exception;
}
