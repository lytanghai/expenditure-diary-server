package com.expenditure_diary.expenditure_diary.service;

import com.expenditure_diary.expenditure_diary.dto.resp.ForexMarketPriceResponse;
import com.expenditure_diary.expenditure_diary.util.DateUtil;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ForexMarketService {

    private final Map<String, Float> history = new ConcurrentHashMap<>();

    public ResponseBuilder<ForexMarketPriceResponse> forexMarketPrice(@RequestBody String symbol) {
        String url = "https://api.gold-api.com/price/" + symbol;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        if(history.size() > 60)
            history.clear();

        ForexMarketPriceResponse forexMarketPriceResponse = new ForexMarketPriceResponse();

        if (result != null) {
            JSONObject response = new JSONObject(result);

            String assetName = response.optString("name", "N/A");
            String symbolName = response.optString("symbol", symbol);
            float price = (float) response.optDouble("price", 0.0);
            String updatedAt = response.optString("updatedAt", "");
            String updatedText = response.optString("updatedAtReadable", "a few seconds ago");

            // Set response
            forexMarketPriceResponse.setAssetName(assetName);
            forexMarketPriceResponse.setSymbol(symbolName);
            forexMarketPriceResponse.setPrice(price);
            forexMarketPriceResponse.setUpdatedAt(DateUtil.format(updatedAt));
            forexMarketPriceResponse.setUpdatedText(updatedText);

            // Store in history
            String historyKey = DateUtil.format(updatedAt);
            history.put(historyKey, price);
        }

        forexMarketPriceResponse.setHistory(history.isEmpty() ? null : new HashMap<>(history));

        return ResponseBuilder.success(forexMarketPriceResponse);
    }


}
