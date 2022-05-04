package com.masflam.monerochad.command;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.Chad;
import com.masflam.monerochad.CommandPath;
import com.masflam.monerochad.command.handler.CommandHandler;
import com.masflam.monerochad.service.CryptowatchService;
import com.masflam.monerochad.service.CryptowatchService.Candle;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.OHLCChart;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("xmr/chart")
public class XmrChartCommand implements CommandHandler {
	
	@Inject
	public CryptowatchService cws;
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		String exchangeStr = event.getOption("exchange").getAsString();
		String[] exchangeData = exchangeStr.split(";");
		String exchange = exchangeData[0];
		String exchangeDisplayName = exchangeData[1];
		String pair = exchangeData[2];
		String pairDisplayName = exchangeData[3];
		String interval = event.getOption("interval").getAsString();
		
		System.out.println(exchangeStr);
		System.out.println(interval);
		
		long intervalSeconds = Long.parseLong(interval);
		long after = System.currentTimeMillis() / 1000 - 30 * intervalSeconds;
		
		List<Candle> candles = cws.getOhlc(exchange, pair, interval, after);
		
		var chart = new OHLCChart(720, 480);
		chart.getStyler().setLegendVisible(false);
		chart.addSeries("XMR/USDT",
			candles.stream().map(c -> new Date(c.timestamp() * 1000)).toList(),
			candles.stream().map(Candle::o).toList(),
			candles.stream().map(Candle::h).toList(),
			candles.stream().map(Candle::l).toList(),
			candles.stream().map(Candle::c).toList(),
			candles.stream().map(Candle::volume).toList()
		);
		
		var baos = new ByteArrayOutputStream();
		BitmapEncoder.saveBitmap(chart, baos, BitmapFormat.PNG);
		
		var builder = new EmbedBuilder()
			.setColor(Chad.ORANGE)
			.setImage("attachment://chart.png")
			.setFooter(pairDisplayName + " at " + exchangeDisplayName);
		
		ihook.sendFile(baos.toByteArray(), "chart.png")
			.addEmbeds(builder.build())
			.queue();
	}
}
