package com.webperside.namazvaxtlaribot.service.impl;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.dto.PrayTimeDto;
import com.webperside.namazvaxtlaribot.enums.Emoji;
import com.webperside.namazvaxtlaribot.models.City;
import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.service.*;
import com.webperside.namazvaxtlaribot.util.Params;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.webperside.namazvaxtlaribot.config.Constants.*;


@Service
@RequiredArgsConstructor
public class MessageCreatorServiceImpl implements MessageCreatorService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private final UserService userService;
    private final SourceService sourceService;
    private final CityService cityService;
    private final SettlementService settlementService;
    private final MessageSource messageSource;
    private final WebscrapService webscrapService;
    private final TaskService taskService;

    @Override
    public String commandNotFoundCreator(String command) {
        return messageSource.getMessage(
                "telegram.command.not_found",
                new Object[]{command},
                Locale.getDefault());
    }

    // test methods
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

    // start and registration methods
    @Override
    public MessageDto startCreator(Long userTgId, String from) {
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

        userService.save(String.valueOf(userTgId));

        return MessageDto.builder()
                .message(startMessage)
                .markup(markup)
                .build();
    }

    @Override
    public String userAlreadyExistCreator(Long userTgId) {

        if (userService.existsByTgId(String.valueOf(userTgId))) {
            return messageSource.getMessage("telegram.user_already_exist", null, Locale.getDefault());
        }

        return null;
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
    public MessageDto selectSourceDescriptionCreator(Integer sourceId, Integer sourcePage) {
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
    public MessageDto selectCityCreator(Integer cityPage, Integer sourceId, Integer sourcePage) {
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

    @Transactional
    @Override
    public MessageDto selectCityDescriptionCreator(Integer cityId, Integer cityPage, Integer sourceId, Integer sourcePage) {
        City city = cityService.getCityById(cityId);
        List<Settlement> settlements = city.getSettlements();
        int size = settlements.size();
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
            return subProcessIfCityHasNoSettlements(settlements.get(0), navButtons);
        } else {
            // {  desc city  }
            // [item1] [item2]
            // [item3] [item4]
            // [prev ] [next ] - not yet
            // [ back to s_c ]
            MessageDto dto = subProcessIfCityHasSettlements(settlements, size, cityPage, sourceId, sourcePage);
            dto.getMarkup().addRow(navButtons.toArray(new InlineKeyboardButton[0]));
            return dto;
        }
    }

    @Transactional
    @Override
    public MessageDto selectCitySettlementDescriptionCreator(Integer settlementId, Integer cityId, Integer cityPage, Integer sourceId, Integer sourcePage) {
        Settlement settlement = settlementService.getById(settlementId);
        String display = settlement.getCity().getName() + "/" + settlement.getName();

        String msg = messageSource.getMessage("telegram.select_city.city_sett_confirm",
                new Object[]{display},
                Locale.getDefault());

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton(BUTTON_T_BACK_TO_SELECT_CITY_MENU)
                        .callbackData(
                                Params.builderWith(BUTTON_CB_SELECT_CITY)
                                        .put(NAVIGATE_TO, BUTTON_CB_NAV_EMPTY)
                                        .put(CITY_PAGE, String.valueOf(cityPage))
                                        .put(SOURCE_ID, String.valueOf(sourceId))
                                        .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                        .build().join()
                        ),
                new InlineKeyboardButton(BUTTON_T_CONFIRM)
                        .callbackData(
                                Params.builderWith(BUTTON_CB_SELECT_CITY_SETT_CONFIRM)
                                        .put(SETT_ID, String.valueOf(settlement.getId()))
                                        .build().join()
                        )
        );

        return MessageDto.builder().message(msg).markup(markup).build();
    }

    @Override
    public String selectCitySettlementConfirmCreator(String from, long userTgId, Integer settlementId) {

        User user = userService.getByTgId(String.valueOf(userTgId));
        Settlement settlement = settlementService.getById(settlementId);

        user.setSettlement(settlement);

        userService.update(user);

        return messageSource.getMessage("telegram.complete_configuration",
                new Object[]{from},
                Locale.getDefault());
    }

    @Override
    public String selectCitySettlementConfirmAfterCreator() {
        return messageSource.getMessage("telegram.complete_configuration.after", null, Locale.getDefault());
    }

    // start and registration methods

    //

    @Override
    @Transactional
    public MessageDto prayTimeCreator(Settlement settlement, Integer settlementId) {

        if (settlement == null) {
            settlement = settlementService.getById(settlementId);
        }

        if (!prayTimes.containsKey(settlement.getId())) {
            subProcessIfTimeNotExists(settlement);
        }

        PrayTimeDto dto = prayTimes.get(settlement.getId());

        String sourceName = settlement.getCity().getSource().getName();
        String msg = null;

        if (sourceName.equals(DS_NAMAZZAMANI_NET)) {
            msg = messageSource.getMessage("telegram.pray_time.namazzamani_net",
                    paramsForNamazZamaniNet(settlement, dto),
                    Locale.getDefault());
        } else if (sourceName.equals(DS_AHLIBEYT_AZ)) {
            msg = messageSource.getMessage("telegram.pray_time.ahlibeyt_az",
                    paramsForAhlibeytAz(settlement, dto),
                    Locale.getDefault());
        }

        return MessageDto.builder().message(msg).build();
    }

    @Override
    @Transactional
    public MessageDto prayTimeByUserIdCreator(long userTgId) {
        User user = userService.getByTgId(String.valueOf(userTgId));
        return prayTimeCreator(user.getSettlement(), null);
    }

    //

    // private util methods

    private MessageDto subProcessIfCityHasNoSettlements(Settlement settlement,
                                                        List<InlineKeyboardButton> navButtons) {
        String msg = messageSource.getMessage("telegram.select_city.city_sett_confirm",
                new Object[]{settlement.getName()},
                Locale.getDefault());

        navButtons.add(new InlineKeyboardButton(BUTTON_T_CONFIRM)
                .callbackData(
                        Params.builderWith(BUTTON_CB_SELECT_CITY_SETT_CONFIRM)
                                .put(SETT_ID, String.valueOf(settlement.getId()))
                                .build().join()
                ));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(navButtons.toArray(new InlineKeyboardButton[0]));

        return MessageDto.builder().message(msg).markup(markup).build();
    }

    private MessageDto subProcessIfCityHasSettlements(List<Settlement> settlements,
                                                      int size,
                                                      Integer cityPage,
                                                      Integer sourceId,
                                                      Integer sourcePage) {
        String msg = messageSource.getMessage("telegram.select_city.has_sett", null, Locale.getDefault());
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (int i = 0; i < size; i += 2) {
            List<InlineKeyboardButton> buttons = new ArrayList<>(2);

            Settlement item1 = settlements.get(i);

            buttons.add(
                    new InlineKeyboardButton(item1.getName())
                            .callbackData(
                                    Params.builderWith(BUTTON_CB_SELECT_CITY_SETT_DESCRIPTION)
                                            .put(SETT_ID, String.valueOf(item1.getId()))
                                            .put(CITY_ID, String.valueOf(item1.getCity().getId()))
                                            .put(CITY_PAGE, String.valueOf(cityPage))
                                            .put(SOURCE_ID, String.valueOf(sourceId))
                                            .put(SOURCE_PAGE, String.valueOf(sourcePage))
                                            .build().join()
                            )
            );

            if (i + 1 < size) {
                Settlement item2 = settlements.get(i + 1);
                buttons.add(
                        new InlineKeyboardButton(item2.getName())
                                .callbackData(
                                        Params.builderWith(BUTTON_CB_SELECT_CITY_SETT_DESCRIPTION)
                                                .put(SETT_ID, String.valueOf(item2.getId()))
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

    private void subProcessIfTimeNotExists(Settlement settlement) {
        PrayTimeDto ptd = new PrayTimeDto();
        Source source = settlement.getCity().getSource();
        String sourceName = source.getName();

        if (sourceName.equals(DS_NAMAZZAMANI_NET)) {
            ptd = ifSourceIsNamazZamaniNet(settlement);
        } else if (sourceName.equals(DS_AHLIBEYT_AZ)) {
            ptd = ifSourceIsAhlibeytAz(settlement);
        }

        prayTimes.put(settlement.getId(), ptd);
    }

    private PrayTimeDto ifSourceIsNamazZamaniNet(Settlement settlement) {
        String params = settlement.getValue();
        String url = settlement.getCity().getSource().getUrl().replace(DS_NAMAZZAMANI_NET_REPLACE, params);
        return webscrapService.prepareDataForNamazZamaniNet(url);
    }

    private Object[] paramsForNamazZamaniNet(Settlement settlement, PrayTimeDto dto) {
        return new Object[]{
                settlement.getName(),
                formatter.format(dto.getImsak()),
                formatter.format(dto.getGunChixir()),
                formatter.format(dto.getZohr()),
                formatter.format(dto.getEsr()),
                formatter.format(dto.getMegrib()),
                formatter.format(dto.getIsha()),
                settlement.getCity().getSource().getName()
        };
    }

    private PrayTimeDto ifSourceIsAhlibeytAz(Settlement settlement) {
        LocalDate ld = LocalDate.now();
        int dayOfMonthPlus1 = ld.getDayOfMonth();

        while (ahlibeytAzTimes.isEmpty()) {
            // If the application runs the first time, data will not be exist
            // First of all, we have to get data from source.
            // After getting data this case will not be true
            webscrapService.prepareDataForAhlibeytAz(settlement.getCity().getSource());
        }

        PrayTimeDto ptd = ahlibeytAzTimes.get(dayOfMonthPlus1);
        return ptd.changeByValue(settlement.getValue());
    }

    private Object[] paramsForAhlibeytAz(Settlement settlement, PrayTimeDto dto) {
        return new Object[]{
                settlement.getName(),
                formatter.format(dto.getImsak()),
                formatter.format(dto.getSubh()),
                formatter.format(dto.getGunChixir()),
                formatter.format(dto.getZohr()),
                formatter.format(dto.getEsr()),
                formatter.format(dto.getGunBatir()),
                formatter.format(dto.getMegrib()),
                formatter.format(dto.getIsha()),
                formatter.format(dto.getGeceYarisi()),
                settlement.getCity().getSource().getName()
        };
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
