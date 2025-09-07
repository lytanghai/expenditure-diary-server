package com.expenditure_diary.expenditure_diary.service;

import com.expenditure_diary.expenditure_diary.component.SettingProperties;
import com.expenditure_diary.expenditure_diary.dto.resp.ForexSignalResp;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ForexSignalService {

    @Autowired private SettingProperties settingProperties;

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

    // ---- EMA ----
    private static double calculateEMA(List<Double> prices, int period) {
        double multiplier = 2.0 / (period + 1);
        double ema = prices.get(0);
        for (int i = 1; i < prices.size(); i++) {
            ema = (prices.get(i) - ema) * multiplier + ema;
        }
        return ema;
    }

    // ---- RSI ----
    private static double calculateRSI(List<Double> prices, int period) {
        double gain = 0, loss = 0;
        for (int i = 1; i < period + 1; i++) {
            double change = prices.get(i) - prices.get(i - 1);
            if (change > 0) gain += change;
            else loss -= change;
        }
        gain /= period;
        loss /= period;

        if (loss == 0) return 100;
        double rs = gain / loss;
        return 100 - (100 / (1 + rs));
    }

    private static String classifyRSI(double rsi) {
        if (rsi < 30) return "Oversold";
        if (rsi < 45) return "Weak Bearish";
        if (rsi <= 55) return "Neutral";
        if (rsi <= 70) return "Weak Bullish";
        return "Overbought";
    }

    // ---- Session ----
    private static String getSession(ZonedDateTime now) {
        int hour = now.getHour();
        if (hour >= 7 && hour < 15) return "London";
        else if (hour >= 20 || hour < 2) return "New York";
        else if (hour >= 2 && hour < 7) return "Asian";
        return "Neutral";
    }

    // ---- ATR ----
    private static double calculateATR(List<Double> highs, List<Double> lows, List<Double> closes, int period) {
        List<Double> trs = new ArrayList<>();
        for (int i = 1; i < closes.size(); i++) {
            double high = highs.get(i);
            double low = lows.get(i);
            double prevClose = closes.get(i - 1);

            double tr = Math.max(high - low,
                    Math.max(Math.abs(high - prevClose), Math.abs(low - prevClose)));
            trs.add(tr);
        }

        if (trs.size() < period) return 0;

        return trs.subList(trs.size() - period, trs.size()).stream()
                .mapToDouble(Double::doubleValue)
                .average().orElse(0);
    }

    // ---- Global WinRate ----
    private static double calculateWinRate(List<Double> closes) {
        int wins = 0, losses = 0;

        for (int i = 20; i < closes.size() - 1; i++) {
            List<Double> subset = closes.subList(0, i + 1);
            double ema20 = calculateEMA(subset, 20);
            double ema50 = calculateEMA(subset, 50);
            double rsi14 = calculateRSI(subset.subList(subset.size() - 15, subset.size()), 14);

            double currentPrice = closes.get(i);
            double nextPrice = closes.get(i + 1);

            if (ema20 > ema50 && rsi14 > 55) {
                if (nextPrice > currentPrice) wins++;
                else losses++;
            } else if (ema20 < ema50 && rsi14 < 45) {
                if (nextPrice < currentPrice) wins++;
                else losses++;
            }
        }

        int total = wins + losses;
        return total > 0 ? (wins * 100.0 / total) : 0;
    }

    // ---- Last N trades ----
    private static String calculateRecentPerformance(List<Double> closes, int lastN) {
        int wins = 0, losses = 0;
        int checked = 0;

        for (int i = closes.size() - lastN - 1; i < closes.size() - 1; i++) {
            if (i < 20) continue;
            List<Double> subset = closes.subList(0, i + 1);
            double ema20 = calculateEMA(subset, 20);
            double ema50 = calculateEMA(subset, 50);
            double rsi14 = calculateRSI(subset.subList(subset.size() - 15, subset.size()), 14);

            double currentPrice = closes.get(i);
            double nextPrice = closes.get(i + 1);

            if (ema20 > ema50 && rsi14 > 55) {
                if (nextPrice > currentPrice) wins++;
                else losses++;
                checked++;
            } else if (ema20 < ema50 && rsi14 < 45) {
                if (nextPrice < currentPrice) wins++;
                else losses++;
                checked++;
            }
        }
        return String.format("Last %d trades: %d Wins / %d Losses (%.2f%%)",
                checked, wins, losses, checked > 0 ? (wins * 100.0 / checked) : 0);
    }

    private static String explainRSI(double rsi) {
        String classification = classifyRSI(rsi);
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

        double ema20 = calculateEMA(closes, 20);
        double ema50 = calculateEMA(closes, 50);
        double rsi14 = calculateRSI(closes.subList(closes.size() - 15, closes.size()), 14);
        String rsiClass = classifyRSI(rsi14);

        double lastPrice = closes.get(closes.size() - 1);
        String signal;
        if (ema20 > ema50 && rsi14 > 55) {
            signal = "Buy at " + lastPrice;
        } else if (ema20 < ema50 && rsi14 < 45) {
            signal = "Sell at " + lastPrice;
        } else {
            signal = "Neutral";
        }

        double atr14 = calculateATR(highs, lows, closes, 14);
        double winRate = calculateWinRate(closes);
        String recentPerf = calculateRecentPerformance(closes, 10);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+7"));
        String session = getSession(now);

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

}
