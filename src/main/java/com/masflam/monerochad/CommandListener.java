package com.masflam.monerochad;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import io.quarkus.logging.Log;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@ApplicationScoped
public class CommandListener extends ListenerAdapter {
	
	@Inject
	@Any
	public Instance<CommandHandler> handler;
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		long eventId = event.getIdLong();
		String cmd = event.getCommandPath();
		Log.tracev("Event {0}: Command {1} called", eventId, cmd);
		Instance<CommandHandler> instance = handler.select(new CommandPath.Literal() {
			@Override
			public String value() {
				return cmd;
			}
		});
		if (!instance.isResolvable()) {
			Log.tracev("Event {0}: Command bean is unresolvable", eventId);
			event.reply("Unknown command").queue();
			return;
		}
		CommandHandler ch = instance.get();
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
}
