package com.expenditure_diary.expenditure_diary.controller;

import com.expenditure_diary.expenditure_diary.dto.resp.ForexSignalResp;
import com.expenditure_diary.expenditure_diary.service.ForexSignalService;
import com.expenditure_diary.expenditure_diary.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("forex")
public class ForexController {

    @Autowired
    private ForexSignalService forexSignalService;

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
    @GetMapping("/analyze")
    public ResponseBuilder<ForexSignalResp> signalProvider(
            @RequestParam(defaultValue = "pair") String pair, @RequestParam(defaultValue = "5") String interval) throws Exception {
        return forexSignalService.signalProvider(pair, interval);
    }
}
