package com.expenditure_diary.expenditure_diary.controller;

import com.expenditure_diary.expenditure_diary.dto.req.FilterReq;
import com.expenditure_diary.expenditure_diary.dto.resp.ForexCalendarResp;
import com.expenditure_diary.expenditure_diary.dto.resp.ForexMarketPriceResponse;
import com.expenditure_diary.expenditure_diary.dto.resp.ForexSignalResp;
import com.expenditure_diary.expenditure_diary.dto.resp.MarketHolidayResponse;
import com.expenditure_diary.expenditure_diary.service.ForexMarketService;
import com.expenditure_diary.expenditure_diary.service.ForexSignalService;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("forex")
public class ForexController {

    @Autowired private ForexSignalService forexSignalService;

    @Autowired private ForexMarketService forexMarketService;

    /**
     * {
     * 	"status": "success",
     * 	"trace_id": null,
     * 	"message": "OK",
     * 	"date": "07-09-2025 18:35:55",
     * 	"data": {
     * 		"pair": "XAU/USD",
     * 		"signal": "Neutral",
     * 		"date": "2025-09-07 18:35",
     * 		"current_session": "Neutral",
     * 		"win_rate": "44.44 % -> Moderate reliability, use extra confirmation before trading.",
     * 		"interval": "5 minutes",
     * 		"rsi": "RSI(14): 44.25 (Weak Bearish) -> (RSI mid-range) = Moderate trend, may follow EMA direction but confirm with volatility.",
     * 		"atr": "ATR(14): 1.80786 (~18079 pips) -> High volatility, big opportunities but also bigger risk. Use tight stop-loss.",
     * 		"recent_performance": "Last 5 trades: 2 Wins / 3 Losses (40.00%)",
     * 		"action": "Strategy historical win rate is low (44.44%). Skip trade."
     *        }
     * }
     * */
    @GetMapping("/ema-rsi-trend")
    public ResponseBuilder<ForexSignalResp> EmaRsiTrend(
            @RequestParam(defaultValue = "pair") String pair, @RequestParam(defaultValue = "5") String interval) throws Exception {
        return forexSignalService.EmaRsiTrend(pair, interval);
    }

    @GetMapping("/breakout-scalper")
    public ResponseBuilder<ForexSignalResp> breakoutScalper(
            @RequestParam("pair") String pair,
            @RequestParam(defaultValue = "5") String interval,
            @RequestParam(defaultValue = "100") String outputSize) {
        return forexSignalService.BreakoutScalper(pair, interval, outputSize);
    }

    @PostMapping("/calendar-event")
    public ResponseBuilder<List<ForexCalendarResp>> calendarEvent(@RequestBody FilterReq filterReq) {
        return forexSignalService.calendarEvent(filterReq);
    }

    @GetMapping("/fetch/price")
    public ResponseBuilder<ForexMarketPriceResponse> fetchForexMarketPrice(@RequestParam(value = "symbol", defaultValue = "XAU") String symbol) {
        return forexMarketService.forexMarketPrice(symbol);
    }

    @PostMapping("/fetch/merket-holiday")
    public ResponseBuilder<MarketHolidayResponse> candleResponse(@RequestParam(value = "exchange", defaultValue = "US") String exchange) {
        return forexMarketService.getMarketHoliday(exchange);
    }
}
