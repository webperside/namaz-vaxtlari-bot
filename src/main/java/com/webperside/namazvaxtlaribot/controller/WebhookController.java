package com.webperside.namazvaxtlaribot.controller;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import com.webperside.namazvaxtlaribot.telegram.TelegramListener;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final TelegramListener listener;

    @PostMapping
    public void webhookRequest(@RequestBody String update){
        listener.filterRequest(BotUtils.parseUpdate(update));
    }
}
