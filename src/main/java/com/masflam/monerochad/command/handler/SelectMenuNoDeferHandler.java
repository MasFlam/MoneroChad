package com.masflam.monerochad.command.handler;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public interface SelectMenuNoDeferHandler {
	void handleSelectMenu(SelectMenuInteractionEvent event, String idData) throws Exception;
}
