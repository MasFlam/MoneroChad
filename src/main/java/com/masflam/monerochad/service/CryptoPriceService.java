package com.masflam.monerochad.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masflam.monerochad.exception.NotFoundException;

import org.apache.commons.collections4.map.LRUMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ApplicationScoped
public class CryptoPriceService {
	
	@RegisterForReflection
	public static record CryptoPrice(
		long retrieved,
		double usd,
		double eur,
		double btc
	) {}
	
	@Inject
	public OkHttpClient httpClient;
	
	@Inject
	public ObjectMapper mapper;
	
	@ConfigProperty(name = "monerochad.price-cache-ttl")
	public Duration priceCacheTtl;
	
	@ConfigProperty(name = "monerochad.price-cache-limit")
	public int priceCacheLimit;
	
	private Map<String, CryptoPrice> priceCache;
	
	@PostConstruct
	public void postConstruct() {
		priceCache = Collections.synchronizedMap(new LRUMap<>(priceCacheLimit));
	}
	
	public CryptoPrice getPrice(String id) throws InterruptedException, ExecutionException, UnsupportedEncodingException {
		return fetchPrice(id).get();
	}
	
	public CompletableFuture<CryptoPrice> fetchPrice(String id) throws UnsupportedEncodingException {
		CryptoPrice cached = priceCache.get(id);
		if (cached != null && System.currentTimeMillis() - cached.retrieved <= priceCacheTtl.toMillis()) {
			return CompletableFuture.completedFuture(cached);
		}
		CompletableFuture<CryptoPrice> future = new CompletableFuture<>();
		httpClient.newCall(
			new Request.Builder()
				.get()
				.header("Accept", "application/json")
				.url("https://api.coingecko.com/api/v3/simple/price?vs_currencies=usd,eur,btc&ids=" + URLEncoder.encode(id, "utf-8"))
				.build()
		).enqueue(new Callback() {
			
			@Override
			public void onFailure(Call call, IOException e) {
				future.completeExceptionally(e);
			}
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				try {
					JsonNode json = mapper.readTree(response.body().byteStream());
					JsonNode data = json.get(id);
					if (data == null) {
						throw new NotFoundException();
					}
					future.complete(new CryptoPrice(
						System.currentTimeMillis(),
						data.get("usd").asDouble(),
						data.get("eur").asDouble(),
						data.get("btc").asDouble()
					));
				} catch (Throwable t) {
					future.completeExceptionally(t);
				}
			}
		});
		return future.thenApply(cp -> {
			priceCache.put(id, cp);
			return cp;
		});
	}
}
