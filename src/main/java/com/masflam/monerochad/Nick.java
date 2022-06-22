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
	
	@Scheduled(every = "PT30S", delayed = "PT10S", identity = "nick-changing")
	public void nick() {
		try {
			CryptoPrice cp = cgs.getPrice("monero");
			String nick = "$%.2f".formatted(cp.usd());
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
