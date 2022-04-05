package com.masflam.monerochad;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import io.quarkus.logging.Log;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@ApplicationScoped
public class CommandListener extends ListenerAdapter {
	
	@Inject
	@Any
	public Instance<CommandHandler> handler;
	
	private CommandHandler resolve(String commandPath) {
		Instance<CommandHandler> instance = handler.select(new CommandPath.Literal() {
			@Override
			public String value() {
				return commandPath;
			}
		});
		if (!instance.isResolvable()) {
			return null;
		}
		return instance.get();
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		long eventId = event.getIdLong();
		String cmd = event.getCommandPath();
		Log.tracev("Event {0}: Command {1} called", eventId, cmd);
		CommandHandler ch = resolve(cmd);
		if (ch == null) {
			event.reply("Unknown command").queue();
			return;
		}
		Log.tracev("Event {0}: Resolved command bean", eventId);
		event.deferReply().queue(ihook -> {
			try {
				ch.handle(event, ihook);
			} catch (Throwable t) {
				Log.errorv(t, "Event {0}: Caught exception while handling command", eventId);
				ihook.sendMessage("An unexpected error ocurred").queue();
			}
		});
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
		long eventId = event.getIdLong();
		String componentId = event.getComponentId();
		Log.tracev("Event {0}: Button `{1}` clicked", eventId, componentId);
		int cmdPathLen = componentId.indexOf(' ');
		String cmd = componentId.substring(0, cmdPathLen);
		String data = componentId.substring(cmdPathLen + 1);
		CommandHandler ch = resolve(cmd);
		if (ch == null) {
			event.reply("Unrecognized button clicked").queue();
			return;
		}
		Log.tracev("Event {0}: Resolved command bean", eventId);
		event.deferReply().queue(ihook -> {
			try {
				ch.handleButton(event, ihook, data);
			} catch (Throwable t) {
				Log.errorv(t, "Event {0}: Caught exception while handling button interaction", eventId);
				ihook.sendMessage("An unexpected error ocurred").queue();
			}
		});
	}
	
	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {
		long eventId = event.getIdLong();
		String componentId = event.getComponentId();
		Log.tracev("Event {0}: Button `{1}` clicked", eventId, componentId);
		int cmdPathLen = componentId.indexOf(' ');
		String cmd = componentId.substring(0, cmdPathLen);
		String data = componentId.substring(cmdPathLen + 1);
		CommandHandler ch = resolve(cmd);
		if (ch == null) {
			event.reply("Unrecognized select menu").queue();
			return;
		}
		Log.tracev("Event {0}: Resolved command bean", eventId);
		event.deferReply().queue(ihook -> {
			try {
				ch.handleSelectMenu(event, ihook, data);
			} catch (Throwable t) {
				Log.errorv(t, "Event {0}: Caught exception while handling SelectMenu interaction", eventId);
				ihook.sendMessage("An unexpected error ocurred").queue();
			}
		});
	}
}
