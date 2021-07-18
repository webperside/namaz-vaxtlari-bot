package com.webperside.namazvaxtlaribot.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionLogDto {

    private Integer id;
    private String userTelegramId;
    private Integer userId;
    private String command;
    private byte status;
    private String message;
    private LocalDateTime createdAt;
}
