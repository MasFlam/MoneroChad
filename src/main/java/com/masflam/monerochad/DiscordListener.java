package com.masflam.monerochad;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.masflam.monerochad.command.handler.ButtonEditHandler;
import com.masflam.monerochad.command.handler.ButtonNoDeferHandler;
import com.masflam.monerochad.command.handler.ButtonReplyHandler;
import com.masflam.monerochad.command.handler.CommandHandler;
import com.masflam.monerochad.command.handler.SelectMenuEditHandler;
import com.masflam.monerochad.command.handler.SelectMenuNoDeferHandler;
import com.masflam.monerochad.command.handler.SelectMenuReplyHandler;

import io.quarkus.logging.Log;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@ApplicationScoped
public class DiscordListener extends ListenerAdapter {
	
	@Inject
	@Any
	public Instance<CommandHandler> commandHandler;
	
	@Inject
	@Any
	public Instance<ButtonNoDeferHandler> buttonNoDeferHandler;
	
	@Inject
	@Any
	public Instance<ButtonEditHandler> buttonEditHandler;
	
	@Inject
	@Any
	public Instance<ButtonReplyHandler> buttonReplyHandler;
	
	@Inject
	@Any
	public Instance<SelectMenuNoDeferHandler> selectMenuNoDeferHandler;
	
	@Inject
	@Any
	public Instance<SelectMenuEditHandler> selectMenuEditHandler;
	
	@Inject
	@Any
	public Instance<SelectMenuReplyHandler> selectMenuReplyHandler;
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		String path = event.getCommandPath();
		var annotation = CommandPath.Literal.of(path);
		var instance = commandHandler.select(annotation);
		if (instance.isResolvable()) {
			var handler = instance.get();
			event.deferReply().queue(ihook -> {
				try {
					handler.handle(event, ihook);
				} catch (Throwable t) {
					ihook.sendMessage("An error ocurred").queue();
					Log.errorf(t, "Error in command handler");
				}
			});
		}
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		String componentId = event.getComponentId();
		int pathLen = componentId.indexOf(' ');
		String path = componentId.substring(0, pathLen);
		String idData = componentId.substring(pathLen);
		
		var annotation = CommandPath.Literal.of(path);
		
		var noDeferInstance = buttonNoDeferHandler.select(annotation);
		if (noDeferInstance.isResolvable()) {
			try {
				noDeferInstance.get().handleButton(event, idData);
			} catch (Throwable t) {
				event.reply("An error ocurred").queue();
				Log.errorf(t, "Error in button handler");
			}
		} else {
			var editInstance = buttonEditHandler.select(annotation);
			if (editInstance.isResolvable()) {
				var handler = editInstance.get();
				event.deferEdit().queue(ihook -> {
					try {
						handler.handleButton(event, ihook, idData);
					} catch (Throwable t) {
						ihook.sendMessage("An error ocurred").queue();
						Log.errorf(t, "Error in button handler");
					}
				});
			} else {
				var replyInstance = buttonReplyHandler.select(annotation);
				if (replyInstance.isResolvable()) {
					var handler = replyInstance.get();
					event.deferReply().queue(ihook -> {
						try {
							handler.handleButton(event, ihook, idData);
						} catch (Throwable t) {
							ihook.sendMessage("An error ocurred").queue();
							Log.errorf(t, "Error in button handler");
						}
					});
				} else {
					event.reply("Unknown button clicked").queue();
				}
			}
		}
	}
	
	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
		String componentId = event.getComponentId();
		int pathLen = componentId.indexOf(' ');
		String path = componentId.substring(0, pathLen);
		String idData = componentId.substring(pathLen);
		
		var annotation = CommandPath.Literal.of(path);
		
		var noDeferInstance = selectMenuNoDeferHandler.select(annotation);
		if (noDeferInstance.isResolvable()) {
			try {
				noDeferInstance.get().handleSelectMenu(event, idData);
			} catch (Throwable t) {
				event.reply("An error ocurred").queue();
				Log.errorf(t, "Error in select menu handler");
			}
		} else {
			var editInstance = selectMenuEditHandler.select(annotation);
			editInstance.get();
			if (editInstance.isResolvable()) {
				var handler = editInstance.get();
				event.deferEdit().queue(ihook -> {
					try {
						handler.handleSelectMenu(event, ihook, idData);
					} catch (Throwable t) {
						ihook.sendMessage("An error ocurred").queue();
						Log.errorf(t, "Error in select menu handler");
					}
				});
			} else {
				var replyInstance = selectMenuReplyHandler.select(annotation);
				if (replyInstance.isResolvable()) {
					var handler = replyInstance.get();
					event.deferReply().queue(ihook -> {
						try {
							handler.handleSelectMenu(event, ihook, idData);
						} catch (Throwable t) {
							ihook.sendMessage("An error ocurred").queue();
							Log.errorf(t, "Error in select menu handler");
						}
					});
				} else {
					event.reply("Unknown select menu").queue();
				}
			}
		}
		
	}
}
