package com.webperside.namazvaxtlaribot.controller.admin;

import com.webperside.namazvaxtlaribot.dto.view.SendMessageDto;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.service.UserService;
import com.webperside.namazvaxtlaribot.util.SortParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ModelAndView users(@RequestParam(value = "p", required = false, defaultValue = "0") Integer page,
                              @RequestParam(value = "sort", required = false, defaultValue = "createdAt:desc") String sortBy,
                              ModelAndView modelAndView){
        String[] sortParams = sortBy.split(":");
        Page<UserDto> userDtoList = userService.getAllWithInfo(page, sortParams);

        modelAndView.addObject("users", userDtoList);
        modelAndView.addObject("sortedBy", SortParams.fieldName(sortParams));
        modelAndView.addObject("sendMessage", new SendMessageDto());
        modelAndView.addObject("sortParam", sortBy);

        modelAndView.setViewName("admin-panel/users");
        return modelAndView;
    }

    @PostMapping("/send-message")
    public String sendMessage(@ModelAttribute(value = "sendMessage") SendMessageDto messageDto){
//        System.out.println(messageDto);
        userService.sendCustomMessage(messageDto);
        return "redirect:/admin/users";
    }
}
