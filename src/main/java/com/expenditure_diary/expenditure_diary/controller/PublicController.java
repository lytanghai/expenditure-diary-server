package com.expenditure_diary.expenditure_diary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("public")
@RestController
public class PublicController {
    @GetMapping("/wakeup")
    public String wakeup() {
        System.out.println("Wake-up request received!");
        return "Server is awake!";
    }
}
