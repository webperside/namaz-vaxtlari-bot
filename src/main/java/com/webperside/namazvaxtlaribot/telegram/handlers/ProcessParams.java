package com.webperside.namazvaxtlaribot.telegram.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessParams {

    private Update update;
    private CallbackQuery query;
    private Integer sourceId;
    private Integer sourcePage;
    private Integer cityId;
    private Integer cityPage;
    private Integer settlementId;
    private String navigateTo;

}
