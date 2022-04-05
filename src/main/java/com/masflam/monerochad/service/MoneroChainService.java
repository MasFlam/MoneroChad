package com.masflam.monerochad.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@ApplicationScoped
public class MoneroChainService {
	
	@RegisterForReflection
	public static record NetworkInfo(
		long retrieved,
		int blockheight,
		String difficulty,
		String cumulativeDifficulty,
		int hardFork,
		long txCount,
		long hashRate,
		long feePerByte
	) {}
	
	@RegisterForReflection
	public static record BlockTransaction(
		String hash,
		boolean coinbase,
		long fee,
		int size
	) {}
	
	@RegisterForReflection
	public static record BlockData(
		long retrieved,
		int height,
		String hash,
		int size,
		long timestamp,
		BlockTransaction[] txs
	) {}
	
	@RegisterForReflection
	public static record Mixin(
		int blockHeight,
		String publicKey,
		String txHash
	) {}
	
	@RegisterForReflection
	public static record Input(
		String keyImage,
		Mixin[] mixins
	) {}
	
	@RegisterForReflection
	public static record Output(
		String publicKey
	) {}
	
	@RegisterForReflection
	public static record TransactionData(
		long retrieved,
		String hash,
		int blockHeight,
		int confirmations,
		boolean coinbase,
		long timestamp,
		long fee,
		int size,
		int rctType,
		int version,
		Input[] inputs,
		Output[] outputs
	) {}
	
	private static final String BASE_URL = "https://blox.minexmr.com/api";
	
	@Inject
	public OkHttpClient httpClient;
	
	@Inject
	public ObjectMapper mapper;
	
	@ConfigProperty(name = "monerochad.network-info-cache-ttl")
	public Duration networkInfoTtl;
	
	private NetworkInfo networkInfoCache;
	
	public NetworkInfo getNetworkInfo() throws InterruptedException, ExecutionException {
		return fetchNetworkInfo().get();
	}
	
	public CompletableFuture<NetworkInfo> fetchNetworkInfo() {
		if (
			networkInfoCache != null &&
			System.currentTimeMillis() - networkInfoCache.retrieved <= networkInfoTtl.toMillis()
		) {
			return CompletableFuture.completedFuture(networkInfoCache);
		}
		CompletableFuture<NetworkInfo> future = new CompletableFuture<>();
		httpClient.newCall(
			new Request.Builder()
				.get()
				.url(BASE_URL + "/networkinfo")
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
					JsonNode data = json.get("data");
					
					future.complete(new NetworkInfo(
						System.currentTimeMillis(),
						data.get("height").asInt(),
						data.get("difficulty").asText(),
						data.get("cumulative_difficulty").asText(),
						data.get("current_hf_version").asInt(),
						data.get("tx_count").asLong(),
						data.get("hash_rate").asLong(),
						// this is fee per *byte* in piconero; it's a bug on their side
						data.get("fee_per_kb").asLong()
					));
				} catch (Throwable t) {
					future.completeExceptionally(t);
				}
			}
		});
		return future.thenApply(ni -> {
			networkInfoCache = ni;
			return ni;
		});
	}
	
	public BlockData getBlockData(String blockHashOrHeight) throws UnsupportedEncodingException, InterruptedException, ExecutionException {
		return fetchBlockData(blockHashOrHeight).get();
	}
	
	public CompletableFuture<BlockData> fetchBlockData(String blockHashOrHeight) throws UnsupportedEncodingException {
		CompletableFuture<BlockData> future = new CompletableFuture<>();
		httpClient.newCall(
			new Request.Builder()
				.get()
				.url(BASE_URL + "/block/" + URLEncoder.encode(blockHashOrHeight, "utf-8"))
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
					JsonNode data = json.get("data");
					
					JsonNode txsNode = data.get("txs");
					int ntxs = txsNode.size();
					var txs = new BlockTransaction[ntxs];
					for (int i = 0; i < ntxs; ++i) {
						JsonNode txNode = txsNode.get(i);
						txs[i] = new BlockTransaction(
							txNode.get("tx_hash").asText(),
							txNode.get("coinbase").asBoolean(),
							txNode.get("tx_fee").asLong(),
							txNode.get("tx_size").asInt()
						);
					}
					future.complete(new BlockData(
						System.currentTimeMillis(),
						data.get("block_height").asInt(),
						data.get("hash").asText(),
						data.get("size").asInt(),
						data.get("timestamp").asLong(),
						txs
					));
				} catch (Throwable t) {
					future.completeExceptionally(t);
				}
			}
		});
		return future;
	}
	
	public TransactionData getTransactionData(String hash) throws UnsupportedEncodingException, InterruptedException, ExecutionException {
		return fetchTransationData(hash).get();
	}
	
	public CompletableFuture<TransactionData> fetchTransationData(String hash) throws UnsupportedEncodingException {
		CompletableFuture<TransactionData> future = new CompletableFuture<>();
		httpClient.newCall(
			new Request.Builder()
				.get()
				.url(BASE_URL + "/transaction/" + URLEncoder.encode(hash, "utf-8"))
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
					JsonNode data = json.get("data");
					
					JsonNode insNode = data.get("inputs");
					int nins = insNode.size();
					var ins = new Input[nins];
					for (int i = 0; i < nins; ++i) {
						String ki = insNode.get(i).get("key_image").asText();
						JsonNode mixinsNode = insNode.get(i).get("mixins");
						int nmixins = mixinsNode.size();
						var mixins = new Mixin[nmixins];
						for (int j = 0; j < nmixins; ++j) {
							mixins[j] = new Mixin(
								mixinsNode.get(j).get("block_no").asInt(),
								mixinsNode.get(j).get("public_key").asText(),
								mixinsNode.get(j).get("tx_hash").asText()
							);
						}
						ins[i] = new Input(ki, mixins);
					}
					
					JsonNode outsNode = data.get("outputs");
					int nouts = outsNode.size();
					var outs = new Output[nouts];
					for (int i = 0; i < nouts; ++i) {
						String pk = outsNode.get(i).get("public_key").asText();
						outs[i] = new Output(pk);
					}
					
					future.complete(new TransactionData(
						System.currentTimeMillis(),
						data.get("tx_hash").asText(),
						data.get("block_height").asInt(),
						data.get("confirmations").asInt(),
						data.get("coinbase").asBoolean(),
						data.get("timestamp").asLong(),
						data.get("tx_fee").asLong(),
						data.get("tx_size").asInt(),
						data.get("rct_type").asInt(),
						data.get("tx_version").asInt(),
						ins,
						outs
					));
				} catch (Throwable t) {
					future.completeExceptionally(t);
				}
			}
		});
		return future;
	}
}
