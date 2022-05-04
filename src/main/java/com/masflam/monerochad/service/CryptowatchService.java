package com.masflam.monerochad.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ApplicationScoped
public class CryptowatchService {
	
	public static record Candle(
		long timestamp,
		double o,
		double h,
		double l,
		double c,
		double volume
	) {}
	
	private static final String BASE_URL = "https://api.cryptowat.ch";
	
	@Inject
	public OkHttpClient httpClient;
	
	@Inject
	public ObjectMapper mapper;
	
	public List<Candle> getOhlc(String exchange, String pair, String interval, long after) throws UnsupportedEncodingException, InterruptedException, ExecutionException {
		return fetchOhlc(exchange, pair, interval, after).get();
	}
	
	public CompletableFuture<List<Candle>> fetchOhlc(String exchange, String pair, String interval, long after) throws UnsupportedEncodingException {
		CompletableFuture<List<Candle>> future = new CompletableFuture<>();
		httpClient.newCall(
			new Request.Builder()
				.get()
				.url(BASE_URL +
					"/markets/" + URLEncoder.encode(exchange, "utf-8") +
					"/" + URLEncoder.encode(pair, "utf-8") +
					"/ohlc?periods=" + URLEncoder.encode(interval, "utf-8") +
					"&after=" + after
				)
				.build()
		).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				future.completeExceptionally(e);
			}
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				try {
					JsonNode data = mapper.readTree(response.body().byteStream());
					System.out.println(data.toPrettyString());
					JsonNode ohlcs = data.get("result").get(interval);
					List<Candle> candles = new ArrayList<>();
					for (JsonNode ohlc : ohlcs) {
						candles.add(new Candle(
							ohlc.get(0).asLong(),
							ohlc.get(1).asDouble(),
							ohlc.get(2).asDouble(),
							ohlc.get(3).asDouble(),
							ohlc.get(4).asDouble(),
							ohlc.get(5).asDouble()
						));
					}
					future.complete(candles);
				} catch (Throwable t) {
					future.completeExceptionally(t);
				}
			}
		});
		return future;
	}
}
