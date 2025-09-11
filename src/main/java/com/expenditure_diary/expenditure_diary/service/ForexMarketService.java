package com.expenditure_diary.expenditure_diary.service;

import com.expenditure_diary.expenditure_diary.dto.resp.ForexMarketPriceResponse;
import com.expenditure_diary.expenditure_diary.dto.resp.MarketHolidayResponse;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

@Service
public class ForexMarketService {

    private final String k = "d3152lpr01qnu2r0igb0d3152lpr01qnu2r0igbg";
    private final String FINHUB_URL = "https://finnhub.io/api/v1/forex";

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

    public ResponseBuilder<MarketHolidayResponse> getMarketHoliday(String exchange) {
        String url = UriComponentsBuilder.fromHttpUrl(FINHUB_URL.concat("/stock/market-holiday"))
                .queryParam("exchange", exchange)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-Finnhub-Token", k);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<MarketHolidayResponse> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, MarketHolidayResponse.class);

        return ResponseBuilder.success(response.getBody());
    }
}
