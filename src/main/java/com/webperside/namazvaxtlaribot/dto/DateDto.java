package com.webperside.namazvaxtlaribot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateDto {

    private String subh;
    private String gunChixir;
    private String zohr;
    private String esr;
    private String axsham;
    private String isha;
}
