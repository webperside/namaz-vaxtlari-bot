package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.dto.view.ActionLogDto;
import com.webperside.namazvaxtlaribot.dto.view.ReportDto;
import com.webperside.namazvaxtlaribot.enums.ActionLogStatus;
import com.webperside.namazvaxtlaribot.models.ActionLog;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.repository.ActionLogRepository;
import com.webperside.namazvaxtlaribot.service.ActionLogService;
import com.webperside.namazvaxtlaribot.service.UserService;
import com.webperside.namazvaxtlaribot.telegram.enums.TelegramCommand;
import com.webperside.namazvaxtlaribot.util.SortParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ActionLogServiceImpl implements ActionLogService {

    private final ActionLogRepository actionLogRepository;
    private final UserService userService;

    @Override
    public void successLog(String userTgId, TelegramCommand command, String message) {
        log(userTgId, command, ActionLogStatus.SUCCESS, message);
    }

    @Override
    public void failedLog(String userTgId, TelegramCommand command, String message) {
        log(userTgId, command, ActionLogStatus.FAILED, message);
    }

    @Override
    public void log(String userTgId, TelegramCommand command, ActionLogStatus actionLogStatus, String message) {
        Integer userId = userService.getUserIdByUserTgId(userTgId);

        String msg = message.length() > 255 ?
                message.substring(0, 255) :
                message;

        actionLogRepository.save(
                ActionLog.builder()
                        .user(User.builder().id(userId).build())
                        .command(command)
                        .status(actionLogStatus)
                        .message(msg)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    @Transactional
    public Page<ActionLogDto> findAllBySearchParams(Integer page, String[] sortParams, Map<String, String> searchParams) {
        Pageable pageable = SortParams.createRequest(page, 20, sortParams);
        Page<ActionLog> actionLogs = actionLogRepository.findAllBySearchParams(pageable, searchParams);
        List<ActionLogDto> actions = new ArrayList<>();

        actionLogs.getContent().forEach(actionLog -> {
            actions.add(ActionLogDto.builder()
                    .id(actionLog.getId())
                    .userTelegramId(actionLog.getUser().getUserTgId())
                    .command(actionLog.getCommand().getCommand())
                    .createdAt(actionLog.getCreatedAt())
                    .status(actionLog.getStatus().getValue())
                    .message(actionLog.getMessage())
                    .build());
        });

        return new PageImpl<>(actions, actionLogs.getPageable(), actionLogs.getTotalElements());
    }

    @Override
    public ReportDto reportActionLog() {
        List<Integer> countSuccess = actionLogRepository.findAllByStatusAndGroupByUserId(ActionLogStatus.SUCCESS);
        List<Integer> countFailed = actionLogRepository.findAllByStatusAndGroupByUserId(ActionLogStatus.FAILED);

        List<Integer> countVaxtlarCommand = actionLogRepository.findAllByCommandAndGroupByUserId(TelegramCommand.VAXTLAR);

        return ReportDto.builder()
                .success(ReportDto.Report_ActionLogDto.fromIntegerList(countSuccess))
                .failed(ReportDto.Report_ActionLogDto.fromIntegerList(countFailed))
                .commandVaxtlar(ReportDto.Report_ActionLogDto.fromIntegerList(countVaxtlarCommand))
                .build();
    }
}
