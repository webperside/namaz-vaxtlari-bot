package com.webperside.namazvaxtlaribot.service.impl;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.enums.Emoji;
import com.webperside.namazvaxtlaribot.models.City;
import com.webperside.namazvaxtlaribot.models.CitySettlement;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.service.*;
import com.webperside.namazvaxtlaribot.util.Params;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.webperside.namazvaxtlaribot.config.Constants.*;


@Service
@RequiredArgsConstructor
public class MessageCreatorServiceImpl implements MessageCreatorService {

    private final UserService userService;
    private final SourceService sourceService;
    private final CityService cityService;
    private final CitySettlementService citySettlementService;
    private final MessageSource messageSource;

    @Override
    public MessageDto testCreator() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(BUTTON_T_BASHLA)
                        .callbackData(
                                Params.builderWith(BUTTON_CB_SELECT_SOURCE)
                                        .put(NAVIGATE_TO, BUTTON_CB_NAV_FIRST_LOAD)
                                        .put(SOURCE_PAGE, "0")
                                        .build()
                                        .join()
                        )
        );

        return MessageDto.builder().message("test").markup(markup).build();
    }

    @Override
    public MessageDto startCreator(String from) {
        String startMessage = messageSource.getMessage("telegram.command.start", new Object[]{from}, Locale.getDefault());
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(BUTTON_T_BASHLA)
                        .callbackData(
                                Params.builderWith(BUTTON_CB_SELECT_SOURCE)
                                        .put(NAVIGATE_TO, BUTTON_CB_NAV_FIRST_LOAD)
                                        .put(SOURCE_PAGE, "0")
                                        .build()
                                        .join()
                        )
        );

        return MessageDto.builder()
                .message(startMessage)
                .markup(markup)
                .build();
    }

    @Override
    public MessageDto selectSourceCreator(Integer sourcePage) {
        Page<Source> sources = sourceService.getAll(sourcePage);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        sources.getContent().forEach(source -> {
            markup.addRow(Collections.singletonList(new InlineKeyboardButton(source.getName())
                    .callbackData(
                            Params.builderWith(BUTTON_CB_SELECT_SOURCE_DESCRIPTION)
                                    .put(SOURCE_ID, String.valueOf(source.getId()))
                                    .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                    .build().join()
                    ))
                    .toArray(new InlineKeyboardButton[0]));
        });

        markup.addRow(createNavigator(sources,
                Params.builderWith(BUTTON_CB_SELECT_SOURCE)
                        .put(SOURCE_PAGE, String.valueOf(sourcePage)))
                .toArray(new InlineKeyboardButton[0])
        );

        String sourceSelect = messageSource.getMessage("telegram.select_source", null, Locale.getDefault());
        return MessageDto.builder()
                .message(sourceSelect)
                .markup(markup)
                .build();
    }

    @Override
    public MessageDto selectSourceDescriptionCreator(Integer sourceId, Integer sourcePage){
        Source source = sourceService.findById(sourceId).orElseThrow(EntityNotFoundException::new);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(Emoji.LEFT_ARROW.getValue())
                        .callbackData(
                                Params.builderWith(BUTTON_CB_SELECT_SOURCE)
                                        .put(NAVIGATE_TO, BUTTON_CB_NAV_EMPTY)
                                        .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                        .build().join()
                        ),
                new InlineKeyboardButton(BUTTON_T_CONFIRM)
                        .callbackData(
                                Params.builderWith(BUTTON_CB_SELECT_CITY)
                                        .put(NAVIGATE_TO, BUTTON_CB_NAV_FIRST_LOAD)
                                        .put(CITY_PAGE, "0")
                                        .put(SOURCE_ID, String.valueOf(sourceId))
                                        .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                        .build().join()
                        )
        );
        return MessageDto.builder()
                .markup(markup)
                .message(source.getDescription())
                .build();
    }

    @Override
    public MessageDto selectCityCreator(Integer cityPage, Integer sourceId, Integer sourcePage){
        Page<City> cities = cityService.getAllBySourceId(sourceId, cityPage);

        if (!cities.hasContent()) {
            return MessageDto.builder().message("Not developed").markup(new InlineKeyboardMarkup()).build();
        }

        int size = cities.getContent().size();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        for (int i = 0; i < size; i += 2) {
            /*
                size 8(7)    size 7(6)
                [1]0 [2]1 or [1]0 [2]1 -> i = 0
                [3]2 [4]3    [3]2 [4]3 -> i = 2
                [5]4 [6]5    [5]4 [6]5 -> i = 4
                [7]6 [8]7    [7]6      -> i = 6
            */

            List<InlineKeyboardButton> buttons = new ArrayList<>(2);

            City item1 = cities.getContent().get(i);
            buttons.add(
                    new InlineKeyboardButton(item1.getName())
                            .callbackData(
                                    Params.builderWith(BUTTON_CB_SELECT_CITY_DESCRIPTION)
                                            .put(CITY_ID, String.valueOf(item1.getId()))
                                            .put(CITY_PAGE, String.valueOf(cityPage))
                                            .put(SOURCE_ID, String.valueOf(sourceId))
                                            .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                            .build().join()
                            )
            );

            if (i + 1 < size) {
                City item2 = cities.getContent().get(i + 1);
                buttons.add(
                        new InlineKeyboardButton(item2.getName())
                                .callbackData(
                                        Params.builderWith(BUTTON_CB_SELECT_CITY_DESCRIPTION)
                                                .put(CITY_ID, String.valueOf(item2.getId()))
                                                .put(CITY_PAGE, String.valueOf(cityPage))
                                                .put(SOURCE_ID, String.valueOf(sourceId))
                                                .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                                .build().join()
                                )
                );
            }

            markup.addRow(buttons.toArray(new InlineKeyboardButton[0]));
        }

        markup.addRow(createNavigator(cities,
                Params.builderWith(BUTTON_CB_SELECT_CITY)
                        .put(CITY_PAGE, String.valueOf(cityPage))
                        .put(SOURCE_ID, String.valueOf(sourceId))
                        .put(SOURCE_PAGE, String.valueOf(sourcePage))
        ).toArray(new InlineKeyboardButton[0]));

        markup.addRow(new InlineKeyboardButton(BUTTON_T_BACK_TO_SELECT_SOURCE_MENU)
                .callbackData(
                        Params.builderWith(BUTTON_CB_SELECT_SOURCE)
                                .put(NAVIGATE_TO, BUTTON_CB_NAV_EMPTY)
                                .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                .build().join()
                )
        );

        String selectCity = messageSource.getMessage("telegram.select_city", null, Locale.getDefault());
        return MessageDto.builder()
                .message(selectCity)
                .markup(markup)
                .build();
    }

    @Override
    public MessageDto selectCityDescriptionCreator(Integer cityId, Integer cityPage, Integer sourceId, Integer sourcePage){
        City city = cityService.getCityById(cityId);
        List<CitySettlement> citySettlements = city.getSettlements();
        int size = citySettlements.size();
        List<InlineKeyboardButton> navButtons = new ArrayList<>();

        navButtons.add(new InlineKeyboardButton(BUTTON_T_BACK_TO_SELECT_CITY_MENU)
                .callbackData(
                        Params.builderWith(BUTTON_CB_SELECT_CITY)
                                .put(NAVIGATE_TO, BUTTON_CB_NAV_EMPTY)
                                .put(CITY_PAGE, String.valueOf(cityPage))
                                .put(SOURCE_ID, String.valueOf(sourceId))
                                .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                .build().join()
                )
        );

        if (size == 1) {
            // {  desc city  }
            // [back][confirm]
            return subProcessIfCityHasNoSettlements(citySettlements.get(0), navButtons);
        } else {
            // {  desc city  }
            // [item1] [item2]
            // [item3] [item4]
            // [prev ] [next ] - not yet
            // [ back to s_c ]
            MessageDto dto = subProcessIfCityHasSettlements(citySettlements, size, cityPage, sourceId, sourcePage);
            dto.getMarkup().addRow(navButtons.toArray(new InlineKeyboardButton[0]));
            return dto;
        }
    }

    @Override
    public String selectCitySettlementConfirmCreator(String from, long userTgId, Integer citySettlementId) {

        User user = userService.getByTgId(String.valueOf(userTgId));
        CitySettlement citySettlement = citySettlementService.getById(citySettlementId);

        user.setCitySettlement(citySettlement);

        userService.update(user);

        return messageSource.getMessage("telegram.complete_configuration",
                new Object[]{from},
                Locale.getDefault());
    }

    // private util methods

    private MessageDto subProcessIfCityHasNoSettlements(CitySettlement cs,
                                                        List<InlineKeyboardButton> navButtons) {
        String msg = messageSource.getMessage("telegram.select_city.city_sett_confirm",
                new Object[]{cs.getCity().getName()},
                Locale.getDefault());

        navButtons.add(new InlineKeyboardButton(BUTTON_T_CONFIRM)
                .callbackData(
                        Params.builderWith(BUTTON_CB_SELECT_CITY_SETT_CONFIRM)
                                .put(CITY_SETT_ID, String.valueOf(cs.getId()))
                                .build().join()
                ));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(navButtons.toArray(new InlineKeyboardButton[0]));

        return MessageDto.builder().message(msg).markup(markup).build();
    }

    private MessageDto subProcessIfCityHasSettlements(List<CitySettlement> citySettlements,
                                                      int size,
                                                      Integer cityPage,
                                                      Integer sourceId,
                                                      Integer sourcePage) {
        String msg = messageSource.getMessage("telegram.select_city.has_sett", null, Locale.getDefault());
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (int i = 0; i < size; i += 2) {
            List<InlineKeyboardButton> buttons = new ArrayList<>(2);

            CitySettlement item1 = citySettlements.get(i);

            buttons.add(
                    new InlineKeyboardButton(item1.getSettlement().getName())
                            .callbackData(
                                    Params.builderWith(BUTTON_CB_SELECT_CITY_SETT_DESCRIPTION)
                                            .put(CITY_SETT_ID, String.valueOf(item1.getId()))
                                            .put(CITY_ID, String.valueOf(item1.getCity().getId()))
                                            .put(CITY_PAGE, String.valueOf(cityPage))
                                            .put(SOURCE_ID, String.valueOf(sourceId))
                                            .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                            .build().join()
                            )
            );

            if (i + 1 < size) {
                CitySettlement item2 = citySettlements.get(i + 1);
                buttons.add(
                        new InlineKeyboardButton(item2.getSettlement().getName())
                                .callbackData(
                                        Params.builderWith(BUTTON_CB_SELECT_CITY_SETT_DESCRIPTION)
                                                .put(CITY_SETT_ID, String.valueOf(item2.getId()))
                                                .put(CITY_ID, String.valueOf(item2.getCity().getId()))
                                                .put(CITY_PAGE, String.valueOf(cityPage))
                                                .put(SOURCE_ID, String.valueOf(sourceId))
                                                .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                                .build().join()
                                )
                );
            }

            markup.addRow(buttons.toArray(new InlineKeyboardButton[0]));
        }
        return MessageDto.builder().message(msg).markup(markup).build();
    }

    private <T> List<InlineKeyboardButton> createNavigator(Page<T> list, Params.Builder builder) {
        List<InlineKeyboardButton> navigator = new ArrayList<>();
        Params.Builder builderRight = builder.copy();

        if (list.hasPrevious()) {
            navigator.add(new InlineKeyboardButton(Emoji.LEFT_ARROW.getValue())
                    .callbackData(builder
                            .put(NAVIGATE_TO, Emoji.LEFT_ARROW.getCallback())
                            .build().join()
                    )
            );
        }

        if (list.hasNext()) {
            navigator.add(new InlineKeyboardButton(Emoji.RIGHT_ARROW.getValue())
                    .callbackData(builderRight
                            .put(NAVIGATE_TO, Emoji.RIGHT_ARROW.getCallback())
                            .build().join()
                    )
            );
        }

        return navigator;
    }
}
