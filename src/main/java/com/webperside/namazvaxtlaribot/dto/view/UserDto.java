package com.webperside.namazvaxtlaribot.dto.view;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Integer id;
    private String userTelegramId;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private UserDto_Settlement settlement;


    @Data
    @Builder
    public static class UserDto_Settlement{
        private Integer id;
        private String name;
    }

}
