package com.webperside.namazvaxtlaribot.config;

import com.webperside.namazvaxtlaribot.dto.PrayTimeDto;
import com.webperside.namazvaxtlaribot.enums.Emoji;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.util.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static final String VALUE_SEPARATOR = "-";
    public static final String CALLBACK_VAL = "CB" + VALUE_SEPARATOR;

    // admin info
    public static final String ADMIN_TELEGRAM_ID = "625929111";

    // ...::: source - namazzamani.net :::...
    public static final String DS_NAMAZZAMANI_NET = "namazzamani.net";
    public static final String DS_NAMAZZAMANI_NET_DEFAULT_CITY_VALUE = "Baki" + VALUE_SEPARATOR;
    public static final String DS_NAMAZZAMANI_NET_CITY_ID = "sehir";
    public static final String DS_NAMAZZAMANI_NET_SETT_ID = "ilce";
    public static final String DS_NAMAZZAMANI_NET_REPLACE = "{REPLACE}";

    // ...::: source - ahlibeyt.az :::...
    public static final String DS_AHLIBEYT_AZ = "ahlibeyt.az";

    // buttons
    public static final String PARAM_SEPARATOR = "&";
    public static final String BUTTON_T_BASHLA = "Başla";
    public static final String BUTTON_T_CONFIRM = "Seç";
    public static final String BUTTON_T_BACK_TO_SELECT_SOURCE_MENU = Emoji.LEFT_ARROW.getValue() + " Mənbə menyusuna geri dön";
    public static final String BUTTON_T_BACK_TO_SELECT_CITY_MENU = Emoji.LEFT_ARROW.getValue() + " Şəhərlər menyusuna geri dön";
    public static final String BUTTON_CB_NAV_FIRST_LOAD = "f_l";
    public static final String BUTTON_CB_NAV_EMPTY = "e";
    public static final String BUTTON_CB_SELECT_SOURCE = "s_s"; // +navigateTo+page
    public static final String BUTTON_CB_SELECT_SOURCE_DESCRIPTION = "s_s_d";
    public static final String BUTTON_CB_SELECT_CITY = "s_c";
    public static final String BUTTON_CB_SELECT_CITY_DESCRIPTION = "s_c_d";
    public static final String BUTTON_CB_SELECT_CITY_SETT_DESCRIPTION = "s_c_s_d";
    public static final String BUTTON_CB_SELECT_CITY_SETT_CONFIRM = "s_c_s_c";


    /**
     * @see Params
     * @implNote constant keys
     */
    public static final String NAVIGATE_TO = "n_t";
    public static final String SOURCE_PAGE  = "s_p";
    public static final String CITY_PAGE = "c_p";
    public static final String SOURCE_ID = "s_i";
    public static final String CITY_ID = "c_i";
    public static final String SETT_ID = "se_i";

    /**
     * @see User
     * @see PrayTimeDto
     * @implNote store data for query optimization
     * prayTimes - Map<Integer, PrayTimeDto>
     */
    public static List<User> users = new ArrayList<>();
    public static Map<Integer, PrayTimeDto> prayTimes = new HashMap<>();
    public static Map<Integer, PrayTimeDto> ahlibeytAzTimes = new HashMap<>();

}
