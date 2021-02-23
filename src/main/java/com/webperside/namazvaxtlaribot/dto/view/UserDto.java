package com.webperside.namazvaxtlaribot.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;
    private String userTelegramId;
    private UserDto_Settlement settlement;

    @Data
    @Builder
    public static class UserDto_Settlement{
        private Integer id;
        private String name;
    }

}
