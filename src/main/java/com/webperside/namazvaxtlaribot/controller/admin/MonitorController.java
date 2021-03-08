package com.webperside.namazvaxtlaribot.controller.admin;

import com.webperside.namazvaxtlaribot.dto.view.ActionLogDto;
import com.webperside.namazvaxtlaribot.dto.view.SourceDto;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.service.ActionLogService;
import com.webperside.namazvaxtlaribot.util.SearchParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final ActionLogService actionLogService;

    @GetMapping
    public ModelAndView sources(@RequestParam(value = "p", required = false, defaultValue = "0") Integer page,
                                @RequestParam(value = "sort", required = false, defaultValue = "createdAt:desc") String sortBy,
                                @RequestParam(value = "search[]", required = false, defaultValue = "") String[] searchParams,
                                ModelAndView modelAndView) {
        String[] sortParams = sortBy.split(":");
        Page<ActionLogDto> actions = actionLogService.findAllBySearchParams(page, sortParams, SearchParams.prepare(searchParams));

        modelAndView.addObject("actions", actions);
        modelAndView.setViewName("admin-panel/monitor");
        return modelAndView;
    }
}
