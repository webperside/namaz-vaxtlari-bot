package com.webperside.namazvaxtlaribot.controller.admin;

import com.webperside.namazvaxtlaribot.dto.view.SourceDto;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/sources")
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @GetMapping
    public ModelAndView sources(ModelAndView modelAndView){
        Page<Source> sources = sourceService.getAll(0);
        modelAndView.addObject("sources", sources);
        modelAndView.addObject("newSource", new SourceDto());
        modelAndView.setViewName("admin-panel/sources");
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editSource(@ModelAttribute(value = "newSource") SourceDto sourceDto){
        sourceService.save(SourceDto.toEntity(sourceDto));
        return "redirect:/admin/sources";
    }
}
