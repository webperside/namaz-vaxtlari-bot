package com.webperside.namazvaxtlaribot.controller.admin;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<UserDto> userDtoList = userService.getAllWithTelegramInfo(0);
        modelAndView.addObject("users", userDtoList);

        modelAndView.setViewName("admin-panel/users");
        return modelAndView;
    }
}
