package com.webperside.namazvaxtlaribot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class CronJobController {

    @GetMapping
    public void trigger() {}
}
