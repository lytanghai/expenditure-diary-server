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

    @GetMapping("/analyze")
    public ResponseBuilder<ForexSignalResp> signalProvider(
            @RequestParam(defaultValue = "pair") String pair, @RequestParam(defaultValue = "5") String interval) throws Exception {
        return forexSignalService.signalProvider(pair, interval);
    }
}
