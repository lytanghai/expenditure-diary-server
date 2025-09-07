package com.expenditure_diary.expenditure_diary.service.group;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ForexAnalyze {

    // ---- EMA ----
    public static double calculateEMA(List<Double> prices, int period) {
        double multiplier = 2.0 / (period + 1);
        double ema = prices.get(0);
        for (int i = 1; i < prices.size(); i++) {
            ema = (prices.get(i) - ema) * multiplier + ema;
        }
        return ema;
    }

    // ---- RSI ----
    public static double calculateRSI(List<Double> prices, int period) {
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

    public static String classifyRSI(double rsi) {
        if (rsi < 30) return "Oversold";
        if (rsi < 45) return "Weak Bearish";
        if (rsi <= 55) return "Neutral";
        if (rsi <= 70) return "Weak Bullish";
        return "Overbought";
    }

    // ---- Session ----
    public static String getSession(ZonedDateTime now) {
        int hour = now.getHour();
        if (hour >= 7 && hour < 15) return "London";
        else if (hour >= 20 || hour < 2) return "New York";
        else if (hour >= 2 && hour < 7) return "Asian";
        return "Neutral";
    }

    // ---- ATR ----
    public static double calculateATR(List<Double> highs, List<Double> lows, List<Double> closes, int period) {
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
    public static double calculateWinRate(List<Double> closes) {
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
    public static String calculateRecentPerformance(List<Double> closes, int lastN) {
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
}
