package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.dto.view.ActionLogDto;
import com.webperside.namazvaxtlaribot.enums.ActionLogStatus;
import com.webperside.namazvaxtlaribot.telegram.enums.TelegramCommand;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ActionLogService {

    void successLog(String userTgId, TelegramCommand command, String message);

    void failedLog(String userTgId, TelegramCommand command, String message);

    void log(String userTgId, TelegramCommand command, ActionLogStatus actionLogStatus, String message);

    Page<ActionLogDto> findAllBySearchParams(Integer page, String[] sortParams, Map<String,String> searchParams);
}
