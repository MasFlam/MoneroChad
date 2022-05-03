package com.masflam.monerochad;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.masflam.monerochad.service.CoinGeckoService;
import com.masflam.monerochad.service.CoinGeckoService.CryptoPrice;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.scheduler.Scheduled;
import net.dv8tion.jda.api.JDA;

@ApplicationScoped
public class Nick {
	
	@Inject
	public CoinGeckoService cgs;
	
	@ConfigProperty(name = "monerochad.guild-ids")
	protected String guildIds;
	
	@Inject
	public JDA jda;
	
	private int cycle = 0;
	
	@Scheduled(every = "PT30S", delayed = "PT10S", identity = "nick-changing")
	public void nick() {
		try {
			CryptoPrice cp = cgs.getPrice("monero");
			if (cycle < 0 || cycle >= 4) {
				cycle = 0;
			}
			String nick = switch (cycle) {
				case 0 -> "$%.2f".formatted(cp.usd());
				case 1 -> "€%.2f".formatted(cp.eur());
				case 2 -> "₿%.8f".formatted(cp.btc());
				case 3 -> "Monero Chad";
				default -> "logic error?";
			};
			++cycle;
			for (String guildId : guildIds.split(",")) {
				jda.getGuildById(guildId).getSelfMember()
					.modifyNickname(nick)
					.queue();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
