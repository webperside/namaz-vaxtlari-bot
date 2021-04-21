package com.webperside.namazvaxtlaribot.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDto {

    private String userTgId;
    private String message;
    private String bulkMessageType;

}
