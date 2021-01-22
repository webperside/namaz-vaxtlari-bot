package com.webperside.namazvaxtlaribot.config;

import com.webperside.namazvaxtlaribot.enums.Emoji;

public class Constants {

    public static final String VALUE_SEPARATOR = "-";
    public static final String CALLBACK_VAL = "CB" + VALUE_SEPARATOR;
    public static final String SOURCE_SELECT_CALLBACK_VAL = CALLBACK_VAL + "ID=";

    // ...::: source - namazzamani.net :::...
    public static final String DS_NAMAZ_ZAMANI_NET_DEFAULT_CITY_VALUE = "Baki" + VALUE_SEPARATOR;
    public static final String DS_NAMAZ_ZAMANI_NET_CITY_ID = "sehir";
    public static final String DS_NAMAZ_ZAMANI_NET_SETT_ID = "ilce";


    // buttons
    public static final String PARAM_SEPARATOR = "&";
    public static final String BUTTON_CB = "CB" + PARAM_SEPARATOR;
    public static final String BUTTON_T_BASHLA = "Başla";
    public static final String BUTTON_T_SELECT_SOURCE_CONFIRM = "Seç";
    public static final String BUTTON_T_BACK_TO_SELECT_SOURCE_MENU = Emoji.LEFT_ARROW.getValue() + " Mənbə menyusuna geri dön";
    public static final String BUTTON_CB_NAV_FIRST_LOAD = "first_load";
    public static final String BUTTON_CB_NAV_EMPTY = "empty";
    public static final String BUTTON_CB_SELECT_SOURCE = BUTTON_CB + "select_source" + PARAM_SEPARATOR; // +navigateTo+page
    public static final String BUTTON_CB_SELECT_SOURCE_DESCRIPTION = BUTTON_CB + "select_source_description" + PARAM_SEPARATOR; // + sourceId + page
    public static final String BUTTON_CB_SELECT_CITY = BUTTON_CB + "select_city" + PARAM_SEPARATOR; // +navigateTo+cityPage+sourceId+sourcePage

}
