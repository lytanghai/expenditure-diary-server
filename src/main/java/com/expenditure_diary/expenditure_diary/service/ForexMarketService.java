package com.expenditure_diary.expenditure_diary.service;

import com.expenditure_diary.expenditure_diary.dto.resp.ForexMarketPriceResponse;
import com.expenditure_diary.expenditure_diary.util.DateUtil;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

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
            String assetName = response.optString("name", "N/A");
            String symbolName = response.optString("symbol", symbol);
            float price = (float) response.optDouble("price", 0.0);
            String updatedText = response.optString("updatedAtReadable");

            forexMarketPriceResponse = new ForexMarketPriceResponse();
            forexMarketPriceResponse.setAssetName(assetName);
            forexMarketPriceResponse.setSymbol(symbolName);
            forexMarketPriceResponse.setPrice(price);
            forexMarketPriceResponse.setUpdatedAt(DateUtil.format(response.optString("updatedAt")));
            forexMarketPriceResponse.setUpdatedText(updatedText);
        }

        return ResponseBuilder.success(forexMarketPriceResponse);
    }

}
