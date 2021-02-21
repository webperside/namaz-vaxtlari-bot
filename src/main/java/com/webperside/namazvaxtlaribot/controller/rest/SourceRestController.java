package com.webperside.namazvaxtlaribot.controller.rest;

import com.webperside.namazvaxtlaribot.dto.view.SourceDto;
import com.webperside.namazvaxtlaribot.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/sources")
@RequiredArgsConstructor
public class SourceRestController {

    private final SourceService sourceService;

    @GetMapping("{sourceId}")
    public SourceDto getById(@PathVariable("sourceId") Integer sourceId){
        return SourceDto.fromEntity(sourceService.findById(sourceId));
    }

}
