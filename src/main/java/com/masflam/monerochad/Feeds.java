package com.masflam.monerochad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@ApplicationScoped
public class Feeds {
	
	private static final String FEED_URL = "https://www.monero.observer/feed-mini.xml";
	private static final String ICON_URL = "https://www.monero.observer/apple-touch-icon.png";
	
	@Inject
	public JDA jda;
	
	@Inject
	public OkHttpClient httpClient;
	
	@Inject
	public SAXReader saxReader;
	
	@ConfigProperty(name = "monerochad.news-channel-ids")
	protected String newsChannelIds;
	
	private List<Long> newsChannels;
	private String prevLink;
	
	@PostConstruct
	public void init() {
		newsChannels = new ArrayList<>();
		for (String id : newsChannelIds.split(",")) {
			newsChannels.add(Long.parseLong(id));
		}
	}
	
	@Scheduled(every = "PT15M", delayed = "PT5S", identity = "news-feeds")
	public void fetchFeeds() throws IOException, DocumentException {
		Log.info("Fetching feeds");
		var call = httpClient.newCall(
			new Request.Builder()
				.get()
				.url(FEED_URL)
				.build()
		);
		try (var resp = call.execute()) {
			Log.info("Executed call");
			Document doc = saxReader.read(resp.body().byteStream());
			List<String> links = doc.selectNodes("/rss/channel/item/link").stream()
				.map(Node::getText).collect(Collectors.toList());
			List<String> titles = doc.selectNodes("/rss/channel/item/title").stream()
				.map(Node::getText).collect(Collectors.toList());
			Log.infof("Prev link: %s", prevLink);
			if (prevLink == null) {
				prevLink = links.get(0);
				return;
			}
			int prevInd = links.indexOf(prevLink);
			prevLink = links.get(0);
			Log.infof("Prev ind: %d", prevInd);
			List<String> toSend = new ArrayList<>();
			List<String> toSendTitles = new ArrayList<>();
			if (prevInd < 0) {
				for (String link : links) {
					toSend.add(link);
				}
				for (String title : titles) {
					toSendTitles.add(title);
				}
			} else {
				for (int i = 0; i < prevInd; ++i) {
					toSend.add(links.get(i));
					toSendTitles.add(titles.get(i));
				}
			}
			Log.info("Sending messages");
			for (long chanId : newsChannels) {
				var chan = jda.getTextChannelById(chanId);
				List<MessageEmbed> embeds = new ArrayList<>();
				for (int i = 0; i < toSend.size(); ++i) {
					String link = toSend.get(i);
					String title = toSendTitles.get(i);
					var embed = new EmbedBuilder()
						.setColor(Chad.ORANGE)
						.setTitle(title, link)
						.setDescription(link)
						.setFooter("Monero Observer", ICON_URL)
						.build();
					embeds.add(embed);
					if (embeds.size() == 10) {
						chan.sendMessageEmbeds(embeds).queue();
						embeds.clear();
					}
				}
				if (!embeds.isEmpty()) {
					chan.sendMessageEmbeds(embeds).queue();
				}
			}
		}
		Log.info("Success! (?)");
	}
}
