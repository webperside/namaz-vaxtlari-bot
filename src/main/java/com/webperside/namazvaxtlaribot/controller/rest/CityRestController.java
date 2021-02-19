package com.webperside.namazvaxtlaribot.controller.rest;

import com.webperside.namazvaxtlaribot.dto.rest.CityDto;
import com.webperside.namazvaxtlaribot.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/cities")
@RequiredArgsConstructor
public class CityRestController {

    private final CityService cityService;

    @GetMapping("{cityId}")
    public CityDto getById(@PathVariable("cityId") Integer cityId){
        return CityDto.fromEntity(cityService.getCityById(cityId));
    }
}
