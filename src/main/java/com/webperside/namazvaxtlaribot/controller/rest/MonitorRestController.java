package com.webperside.namazvaxtlaribot.controller.rest;

import com.webperside.namazvaxtlaribot.dto.view.ReportDto;
import com.webperside.namazvaxtlaribot.service.ActionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/monitor")
@RequiredArgsConstructor
public class MonitorRestController {

    private final ActionLogService actionLogService;

    @GetMapping
    public ReportDto report(){
        return actionLogService.reportActionLog();
    }
}
