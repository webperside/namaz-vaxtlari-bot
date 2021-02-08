package com.webperside.namazvaxtlaribot.dto.rest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelegramUserByIdDto {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
}
