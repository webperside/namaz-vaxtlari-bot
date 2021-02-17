package com.webperside.namazvaxtlaribot.telegram.handlers;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;

import java.util.Map;

import static com.webperside.namazvaxtlaribot.config.Constants.*;

public interface HandlerInterface {

    void process(ProcessParams processParams);

    default void run(Update update, CallbackQuery query, Map<String, String> values) {
        Integer sourceId = Integer.valueOf(values.getOrDefault(SOURCE_ID, "0"));
        Integer sourcePage = Integer.parseInt(values.getOrDefault(SOURCE_PAGE, "0"));
        Integer cityId = Integer.parseInt(values.getOrDefault(CITY_ID, "0"));
        Integer cityPage = Integer.parseInt(values.getOrDefault(CITY_PAGE, "0"));
        Integer settlementId = Integer.parseInt(values.getOrDefault(SETT_ID, "0"));
        String navigateTo = values.getOrDefault(NAVIGATE_TO, null);

        // call implement method
        process(ProcessParams.builder()
                .update(update)
                .query(query)
                .sourceId(sourceId)
                .sourcePage(sourcePage)
                .cityId(cityId)
                .cityPage(cityPage)
                .settlementId(settlementId)
                .navigateTo(navigateTo)
                .build());
    }

    default void run(Update update, CallbackQuery query, String customMessage){
        // call implement method
        process(ProcessParams.builder()
                .update(update)
                .query(query)
                .customMessage(customMessage)
                .build());
    }

}
