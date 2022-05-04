package com.masflam.monerochad.command;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandNoDeferHandler;
import com.masflam.monerochad.service.CryptowatchService;
import com.masflam.monerochad.service.CryptowatchService.Candle;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.OHLCChart;

import io.quarkus.logging.Log;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@ApplicationScoped
@CommandPath("xmr/chart")
public class XmrChartCommand implements CommandNoDeferHandler {
	
	private static final long COOLDOWN_MS = 30_000L;
	
	@Inject
	public CryptowatchService cws;
	
	private long prevMillis = 0;
	
	@Override
	public void handle(SlashCommandInteractionEvent event) throws Exception {
		long currentMillis = System.currentTimeMillis();
		if (currentMillis - prevMillis < COOLDOWN_MS) {
			String msg = "The command is on cooldown. Try again in **%d** seconds."
				.formatted((prevMillis + COOLDOWN_MS - currentMillis) / 1000L);
			event.replyEmbeds(Chad.failEmbed(msg).build())
				.setEphemeral(true)
				.queue();
			return;
		}
		prevMillis = System.currentTimeMillis();
		event.deferReply().queue(ihook -> {
			try {
				String exchangeStr = event.getOption("exchange").getAsString();
				String[] exchangeData = exchangeStr.split(";");
				String exchange = exchangeData[0];
				String exchangeDisplayName = exchangeData[1];
				String pair = exchangeData[2];
				String pairDisplayName = exchangeData[3];
				
				String intervalStr = event.getOption("interval").getAsString();
				String[] intervalData = intervalStr.split(";");
				String interval = intervalData[0];
				String intervalDisplayName = intervalData[1];
				
				long intervalSeconds = Long.parseLong(interval);
				long after = System.currentTimeMillis() / 1000 - 30 * intervalSeconds;
				
				List<Candle> candles = cws.getOhlc(exchange, pair, interval, after);
				
				var chart = new OHLCChart(720, 480);
				chart.getStyler().setLegendVisible(false);
				chart.addSeries("moonero",
					candles.stream().map(c -> new Date(c.timestamp() * 1000)).toList(),
					candles.stream().map(Candle::o).toList(),
					candles.stream().map(Candle::h).toList(),
					candles.stream().map(Candle::l).toList(),
					candles.stream().map(Candle::c).toList()
				);
				
				var baos = new ByteArrayOutputStream();
				BitmapEncoder.saveBitmap(chart, baos, BitmapFormat.PNG);
				
				var builder = new EmbedBuilder()
				.setColor(Chad.ORANGE)
					.setImage("attachment://chart.png")
					.setFooter("%s (%s) at %s".formatted(pairDisplayName, intervalDisplayName, exchangeDisplayName));
				
				ihook.sendFile(baos.toByteArray(), "chart.png")
					.addEmbeds(builder.build())
					.queue();
			} catch (Throwable t) {
				ihook.sendMessageEmbeds(Chad.failEmbed("An error ocurred").build()).queue();
				Log.errorf(t, "Error in /xmr/chart");
			}
		});
	}
}
