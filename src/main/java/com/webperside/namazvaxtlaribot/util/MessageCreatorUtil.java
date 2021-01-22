package com.webperside.namazvaxtlaribot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.enums.Emoji;
import com.webperside.namazvaxtlaribot.models.City;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.service.CityService;
import com.webperside.namazvaxtlaribot.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.webperside.namazvaxtlaribot.config.Constants.*;

@Service
@RequiredArgsConstructor
public class MessageCreatorUtil {

    private final SourceService sourceService;
    private final CityService cityService;
    private final MessageSource messageSource;

    public MessageDto selectSourceCreator(Integer page) {
        Page<Source> sources = sourceService.getAll(page);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        sources.getContent().forEach(source -> {
            markup.addRow(Collections.singletonList(new InlineKeyboardButton(source.getName())
                    .callbackData(BUTTON_CB_SELECT_SOURCE_DESCRIPTION + source.getId() + PARAM_SEPARATOR + page))
                    .toArray(new InlineKeyboardButton[0]));
        });

        markup.addRow(createNavigator(sources, BUTTON_CB_SELECT_SOURCE, new String[]{String.valueOf(page)}).toArray(new InlineKeyboardButton[0]));

        String sourceSelect = messageSource.getMessage("telegram.select_source", null, Locale.getDefault());
        return MessageDto.builder()
                .message(sourceSelect)
                .markup(markup)
                .build();
    }

    public MessageDto selectCityCreator(Integer cityPage, Integer sourceId, Integer sourcePage) {
        Page<City> cities = cityService.getAllBySourceId(sourceId, cityPage);

        if(!cities.hasContent()){
            return MessageDto.builder().message("Not developed").markup(new InlineKeyboardMarkup()).build();
        }

        int size = cities.getSize();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        for (int i = 0; i < size; i += 2) {
            List<InlineKeyboardButton> buttons = new ArrayList<>(2);

            City item1 = cities.getContent().get(i);
            buttons.add(
                    new InlineKeyboardButton(item1.getName())
                            .callbackData("test" + item1.getId())
            );

            /*
                size 8(7)    size 7(6)
                [1]0 [2]1 or [1]0 [2]1 -> i = 0
                [3]2 [4]3    [3]2 [4]3 -> i = 2
                [5]4 [6]5    [5]4 [6]5 -> i = 4
                [7]6 [8]7    [7]6      -> i = 6
            */
            try {
                City item2 = cities.getContent().get(i + 1);
                buttons.add(
                        new InlineKeyboardButton(item2.getName())
                                .callbackData("test" + item2.getId())
                );
            } catch (IndexOutOfBoundsException ex) {
                System.out.println(ex.getMessage());
            }

            markup.addRow(buttons.toArray(new InlineKeyboardButton[0]));

        }

        String[] params = new String[]{String.valueOf(cityPage), String.valueOf(sourceId), String.valueOf(sourcePage)};
        markup.addRow(createNavigator(cities, BUTTON_CB_SELECT_CITY, params).toArray(new InlineKeyboardButton[0]));
        markup.addRow(new InlineKeyboardButton(BUTTON_T_BACK_TO_SELECT_SOURCE_MENU)
                .callbackData(BUTTON_CB_SELECT_SOURCE+BUTTON_CB_NAV_EMPTY+PARAM_SEPARATOR+sourcePage));

        String selectCity = messageSource.getMessage("telegram.select_city", null, Locale.getDefault());
        return MessageDto.builder()
                .message(selectCity)
                .markup(markup)
                .build();
    }

    private <T> List<InlineKeyboardButton> createNavigator(Page<T> list, String callback, String[] params) {
        List<InlineKeyboardButton> navigator = new ArrayList<>();
        String prepared = prepareParams(params);

        if (list.hasPrevious()) {
            navigator.add(new InlineKeyboardButton(Emoji.LEFT_ARROW.getValue())
                    .callbackData(callback + Emoji.LEFT_ARROW.getCallback() + PARAM_SEPARATOR + prepared)
            );
        }

        if (list.hasNext()) {
            navigator.add(new InlineKeyboardButton(Emoji.RIGHT_ARROW.getValue())
                    .callbackData(callback + Emoji.RIGHT_ARROW.getCallback() + PARAM_SEPARATOR + prepared)
            );
        }

        return navigator;
    }

    public String prepareParams(String[] params) {
        return String.join(PARAM_SEPARATOR, params);
    }
}
