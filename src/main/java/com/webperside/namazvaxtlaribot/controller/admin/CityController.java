package com.webperside.namazvaxtlaribot.controller.admin;

import com.webperside.namazvaxtlaribot.dto.view.CityDto;
import com.webperside.namazvaxtlaribot.dto.view.SourceDto;
import com.webperside.namazvaxtlaribot.models.City;
import com.webperside.namazvaxtlaribot.service.CityService;
import com.webperside.namazvaxtlaribot.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    private final SourceService sourceService;

    @GetMapping
    public ModelAndView cities(@RequestParam(value = "sid", required = false) Integer sid,
                               @RequestParam(value = "p", required = false, defaultValue = "0") Integer page,
                               ModelAndView modelAndView){

        List<SourceDto> sources = new ArrayList<>();
        Page<City> cities = Page.empty();

        if(sid == null){
            sources = sourceService.getAllShortInfo(0);
            modelAndView.addObject("sourceSelected", false);
        } else {
            cities = cityService.getAllBySourceId(sid, page, 10);
            modelAndView.addObject("sourceSelected", true);
        }

        modelAndView.addObject("sources", sources);
        modelAndView.addObject("cities", cities);
        modelAndView.addObject("newCity", new CityDto());

        modelAndView.setViewName("admin-panel/cities");
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editSource(@RequestParam ("sid") Integer sourceId,
                             @ModelAttribute(value = "newCity") CityDto cityDto){
//        System.out.println(cityDto.getSettlements());
        cityService.save(CityDto.toEntity(cityDto));
        return "redirect:/admin/cities?sid="+sourceId;
    }

}
