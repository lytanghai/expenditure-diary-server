package com.expenditure_diary.expenditure_diary.service;

import com.expenditure_diary.expenditure_diary.dto.resp.CandleResponse;
import com.expenditure_diary.expenditure_diary.dto.resp.ForexMarketPriceResponse;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ForexMarketService {

    private final String k = "d3152lpr01qnu2r0igb0d3152lpr01qnu2r0igbg"; // Replace with your Finnhub API key
    private final String FINHUB_URL = "https://finnhub.io/api/v1/forex/candle";

    public ResponseBuilder<ForexMarketPriceResponse> forexMarketPrice(@RequestBody String symbol) {
        String url = "https://api.gold-api.com/price/".concat(symbol);

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        ForexMarketPriceResponse forexMarketPriceResponse = null;

        if (result != null) {
            JSONObject response = new JSONObject(result);

            // Parse fields safely
            String assetName = response.optString("name", "N/A");
            String symbolName = response.optString("symbol", symbol);
            float price = (float) response.optDouble("price", 0.0);
            String updatedAt = response.optString("updatedAt");
            String updatedText = response.optString("updatedAtReadable");

            // Convert updatedAt (UTC → Asia/Phnom_Penh)
            LocalDateTime phnomPenhDateTime = null;
            if (updatedAt != null && !updatedAt.isEmpty()) {
                Instant instant = Instant.parse(updatedAt);
                phnomPenhDateTime = instant.atZone(ZoneId.of("Asia/Phnom_Penh")).toLocalDateTime();
            }

            // Build response object
            forexMarketPriceResponse = new ForexMarketPriceResponse();
            forexMarketPriceResponse.setAssetName(assetName);
            forexMarketPriceResponse.setSymbol(symbolName);
            forexMarketPriceResponse.setPrice(price);
            forexMarketPriceResponse.setUpdatedAt(phnomPenhDateTime);
            forexMarketPriceResponse.setUpdatedText(updatedText);
        }

        return ResponseBuilder.success(forexMarketPriceResponse);
    }

    public ResponseBuilder<CandleResponse> getCandleData(String fromSymbol, String toSymbol, String resolution, long from, long to) {
        String url = String.format("%s?symbol=%s%s&resolution=%s&from=%d&to=%d&token=%s",
                FINHUB_URL, fromSymbol, toSymbol, resolution, from, to, k);

        RestTemplate restTemplate = new RestTemplate();
        return ResponseBuilder.success(restTemplate.getForObject(url, CandleResponse.class));
    }

}
