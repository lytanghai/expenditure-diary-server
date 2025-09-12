package com.expenditure_diary.expenditure_diary.service;

import com.expenditure_diary.expenditure_diary.dto.resp.CmcPriceResp;
import com.expenditure_diary.expenditure_diary.dto.resp.ForexMarketPriceResponse;
import com.expenditure_diary.expenditure_diary.util.DateUtil;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ForexMarketService {

    private static final Logger log = LoggerFactory.getLogger(ForexMarketService.class);
    private final Map<String, Float> history = new ConcurrentHashMap<>();

    @Autowired
    private RestTemplateService restTemplateService;

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

    public ResponseBuilder<CmcPriceResp> fetchMarketCmcPrice(String symbol, String convert) {
        String url = UriComponentsBuilder.fromHttpUrl("https://pro-api.coinmarketcap.com/v3/cryptocurrency/quotes/latest")
                .queryParam("symbol", symbol)
                .queryParam("convert", convert)
                .toUriString();

        Map<String, String> headers = new HashMap<>();
        headers.put("X-CMC_PRO_API_KEY", "7e9ed58c-b1b8-4eaa-81ed-c36781c2cd68");
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        ResponseEntity<String> response = restTemplateService.callExternalApi(
                url,
                HttpMethod.GET,
                headers,
                null,
                String.class);
        String responseStr = response.getBody();

        JSONObject obj = new JSONObject(responseStr);

        JSONObject data = new  JSONObject(obj.getJSONArray("data").get(0).toString());
        CmcPriceResp respObj = new CmcPriceResp();

        respObj.setId(data.optInt("id"));
        respObj.setName(data.optString("name"));
        respObj.setFromSymbol(data.optString("symbol"));

        JSONObject quoteObj = new JSONObject(data.optJSONArray("quote").get(0).toString());

        CmcPriceResp.Quote quote = new CmcPriceResp.Quote();
        quote.setToSymbol(quoteObj.optString("symbol"));
        quote.setPrice(quoteObj.optFloat("price"));
        quote.setVolume24Hour(quoteObj.optFloat("volume_24h"));
        quote.setVolumeChange24Hour(quoteObj.optFloat("volume_change_24h"));
        quote.setPercentChangeIn1Hour(quoteObj.optFloat("percent_change_1h"));
        quote.setPercentChangeIn24Hour(quoteObj.optFloat("percent_change_24h"));
        quote.setPercentChangeIn7Day(quoteObj.optFloat("percent_change_7d"));
        quote.setPercentChangeIn30Day(quoteObj.optFloat("percent_change_30d"));
        quote.setPercentChangeIn60Day(quoteObj.optFloat("percent_change_60d"));
        quote.setPercentChangeIn90Day(quoteObj.optFloat("percent_change_90d"));
        quote.setLastUpdated(DateUtil.format(quoteObj.optString("last_updated")));

        respObj.setQuote(quote);

        return ResponseBuilder.success(respObj);
    }

}
