package com.webperside.namazvaxtlaribot.controller.admin;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.dto.view.SendMessageDto;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ModelAndView users(ModelAndView modelAndView){
        List<UserDto> userDtoList = userService.getAllWithInfo(0);
        modelAndView.addObject("users", userDtoList);

        modelAndView.addObject("sendMessage", new SendMessageDto());

        modelAndView.setViewName("admin-panel/users");
        return modelAndView;
    }

    @PostMapping("/send-message")
    public String sendMessage(@ModelAttribute(value = "sendMessage") SendMessageDto messageDto){
        userService.sendCustomMessage(messageDto);
        return "redirect:/admin/users";
    }
}
