package com.webperside.namazvaxtlaribot.controller.rest;

import com.webperside.namazvaxtlaribot.dto.view.UserTelegramInfoDto;
import com.webperside.namazvaxtlaribot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("{userTgId}")
    public UserTelegramInfoDto getTelegramInfoById(@PathVariable("userTgId") String userTgId){
        return userService.getTelegramInfoByUserId(userTgId);
    }

}
