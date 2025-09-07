package com.expenditure_diary.expenditure_diary.service;

import com.expenditure_diary.expenditure_diary.cache.ForexCalendarCache;
import com.expenditure_diary.expenditure_diary.component.SettingProperties;
import com.expenditure_diary.expenditure_diary.dto.req.FilterReq;
import com.expenditure_diary.expenditure_diary.dto.resp.ForexCalendarResp;
import com.expenditure_diary.expenditure_diary.dto.resp.ForexSignalResp;
import com.expenditure_diary.expenditure_diary.service.group.ForexAnalyze;
import com.expenditure_diary.expenditure_diary.util.DateUtil;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForexSignalService {

    private final Logger log = LoggerFactory.getLogger(ForexSignalService.class);

    @Autowired private SettingProperties settingProperties;

    private static final ForexCalendarCache cache = new ForexCalendarCache();

    // ---- Fetch OHLC data ----
    private List<Double> closes = new ArrayList<>();
    private List<Double> highs = new ArrayList<>();
    private List<Double> lows = new ArrayList<>();

    private void fetchOhlc(String pair, String interval) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = settingProperties.getTwelveDataUrl()
                .replaceAll("pair", pair)
                .replaceAll("itz", interval);

        String json = restTemplate.getForObject(url + settingProperties.getTwelveDataApiKey(),
                String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json).get("values");

        closes.clear(); highs.clear(); lows.clear();
        for (JsonNode node : root) {
            closes.add(Double.parseDouble(node.get("close").asText()));
            highs.add(Double.parseDouble(node.get("high").asText()));
            lows.add(Double.parseDouble(node.get("low").asText()));
        }
        Collections.reverse(closes);
        Collections.reverse(highs);
        Collections.reverse(lows);
    }

    private static String explainRSI(double rsi) {
        String classification = ForexAnalyze.classifyRSI(rsi);
        if (rsi < 30) {
            return String.format("RSI(14): %.2f (%s) -> (RSI <30) = Usually oversold = risk of reversal upward (buy bounce), not sell.", rsi, classification);
        } else if (rsi > 70) {
            return String.format("RSI(14): %.2f (%s) -> (RSI >70) = Usually overbought = risk of reversal downward (sell bounce), not buy.", rsi, classification);
        } else if (rsi >= 45 && rsi <= 55) {
            return String.format("RSI(14): %.2f (%s) -> (RSI between 45-55) = Neutral momentum, avoid trading until trend direction is clear.", rsi, classification);
        } else {
            return String.format("RSI(14): %.2f (%s) -> (RSI mid-range) = Moderate trend, may follow EMA direction but confirm with volatility.", rsi, classification);
        }
    }

    private static String explainATR(double atr) {
        double pips = atr * 10000;
        if (pips < 5) {
            return String.format("ATR(14): %.5f (~%.0f pips) -> Very low volatility, spreads may eat profit. Avoid scalping.", atr, pips);
        } else if (pips < 15) {
            return String.format("ATR(14): %.5f (~%.0f pips) -> Normal volatility, suitable for short scalps.", atr, pips);
        } else {
            return String.format("ATR(14): %.5f (~%.0f pips) -> High volatility, big opportunities but also bigger risk. Use tight stop-loss.", atr, pips);
        }
    }

    private static String explainWinRate(double winRate) {
        if (winRate < 40) {
            return String.format("%.2f %% -> Very poor performance historically, avoid following blindly.", winRate);
        } else if (winRate < 60) {
            return String.format("%.2f %% -> Moderate reliability, use extra confirmation before trading.", winRate);
        } else {
            return String.format("%.2f %% -> Good historical reliability, signals more trustworthy.", winRate);
        }
    }

    // ---- Main Provider ----
    public ResponseBuilder<ForexSignalResp> signalProvider(String pair, String interval) throws Exception {

        fetchOhlc(pair, interval);
        double ema20 = ForexAnalyze.calculateEMA(closes, 20);
        double ema50 = ForexAnalyze.calculateEMA(closes, 50);
        double rsi14 = ForexAnalyze.calculateRSI(closes.subList(closes.size() - 15, closes.size()), 14);

        double lastPrice = closes.get(closes.size() - 1);
        String signal;
        if (ema20 > ema50 && rsi14 > 55) {
            signal = "Buy at " + lastPrice;
        } else if (ema20 < ema50 && rsi14 < 45) {
            signal = "Sell at " + lastPrice;
        } else {
            signal = "Neutral";
        }

        double atr14 = ForexAnalyze.calculateATR(highs, lows, closes, 14);
        double winRate = ForexAnalyze.calculateWinRate(closes);
        String recentPerf = ForexAnalyze.calculateRecentPerformance(closes, 10);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+7"));
        String session = ForexAnalyze.getSession(now);

        // ---- Decide Action ----
        String action;
        if (rsi14 < 30 && signal.startsWith("Sell")) {
            action = "Avoid selling because RSI shows oversold — wait for possible reversal upward.";
        } else if (rsi14 > 70 && signal.startsWith("Buy")) {
            action = "Avoid buying because RSI shows overbought — wait for possible reversal downward.";
        } else if (atr14 < 0.0005) {
            action = "Market volatility is too low (~<5 pips). Avoid scalping now.";
        } else if (winRate < 50.0) {
            action = "Strategy historical win rate is low (" + String.format("%.2f", winRate) + "%). Skip trade.";
        } else if (recentPerf.contains("(0.00%")) {
            action = "Recent trades show 0% success. Market conditions changed, avoid trading now.";
        } else {
            action = "Conditions look acceptable. You may follow the signal: " + signal;
        }

        ForexSignalResp forexSignalResp = new ForexSignalResp();
        forexSignalResp.setPair(pair);
        forexSignalResp.setSignal(signal);
        forexSignalResp.setCurrentSession(session);
        forexSignalResp.setInterval(interval.concat(" minutes"));
        forexSignalResp.setDate(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        forexSignalResp.setWinRate(explainWinRate(winRate));
        forexSignalResp.setRsi(explainRSI(rsi14));
        forexSignalResp.setAtr(explainATR(atr14));

        forexSignalResp.setRecentPerformance(recentPerf);
        forexSignalResp.setAction(action);

        return ResponseBuilder.success(forexSignalResp);
    }

    private List<ForexCalendarResp> filtered(List<ForexCalendarResp> raw, FilterReq filterReq) {
        return raw.stream()
                .filter(event -> {
                    String country = filterReq.getCountry();
                    return country == null || country.isEmpty() || event.getCountry().equalsIgnoreCase(country);
                })
                .filter(event -> {
                    String impact = filterReq.getImpact();
                    return impact == null || impact.isEmpty() || event.getImpact().equalsIgnoreCase(impact);
                })
                .filter(event -> {
                    String date = filterReq.getDate();
                    return date == null || date.isEmpty() || event.getDate().substring(0, 10).equals(date);
                })
                .filter(event -> {
                    String title = filterReq.getTitle();
                    if (title == null || title.isEmpty()) return true;

                    String[] keywords = title.toLowerCase().split("\\s+");
                    String eventTitle = event.getTitle().toLowerCase();
                    return Arrays.stream(keywords).anyMatch(eventTitle::contains);
                })
                .collect(Collectors.toList());
    }

    public ResponseBuilder<List<ForexCalendarResp>> calendarEvent(FilterReq filterReq) {

        if (!cache.getAll().isEmpty()) {
            log.info("fetch from cache!");
            return ResponseBuilder.success(filtered(cache.getAll(), filterReq));
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = settingProperties.getForexFactoryThisWeekJson() == null
                ? "https://nfs.faireconomy.media/ff_calendar_thisweek.json"
                : settingProperties.getForexFactoryThisWeekJson();

        String resultStr = restTemplate.getForObject(url, String.class);

        JSONArray result = new JSONArray(resultStr);

        List<ForexCalendarResp> responseList = new ArrayList<>();
        for(Object i: result) {
            JSONObject each = (JSONObject) i;
            ForexCalendarResp eachResp = new ForexCalendarResp();
            eachResp.setDate(DateUtil.toPhnomPenhTime(each.optString("date").replaceAll("ICT","")));
            eachResp.setCountry(each.optString("country"));
            eachResp.setForecast(each.optString("forecast"));
            eachResp.setImpact(each.optString("impact"));
            eachResp.setTitle(each.optString("title"));
            eachResp.setPrevious(each.optString("previous"));
            eachResp.setActual(each.optString("actual", null));
            responseList.add(eachResp);
            cache.put(eachResp);
        }

        return ResponseBuilder.success(filtered(responseList, filterReq));
    }

}
