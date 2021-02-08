package com.webperside.namazvaxtlaribot.controller.rest;

import com.webperside.namazvaxtlaribot.dto.rest.TelegramUserByIdDto;
import com.webperside.namazvaxtlaribot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram/user")
@RequiredArgsConstructor
public class TelegramUserController {

    private final UserService userService;

    @GetMapping("{tgUserId}")
    public TelegramUserByIdDto getUserInfoByTgId(@PathVariable String tgUserId){
        return userService.getUserInfoByTgIdFromTelegram(tgUserId);
    }
}
