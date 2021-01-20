package com.webperside.namazvaxtlaribot.config;

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
    public static final String BUTTON_CB_BASHLA = BUTTON_CB + "bashla";
    public static final String BUTTON_T_BASHLA = "Başla";
    public static final String BUTTON_CB_SELECT_SOURCE = BUTTON_CB + "select_source"; // + sourceId
    public static final String BUTTON_CB_SELECT_SOURCE_NAVIGATE = BUTTON_CB + "select_source_navigate" + PARAM_SEPARATOR; // + navigateValue
    public static final String BUTTON_CB_SELECT_SOURCE_DESCRIPTION = BUTTON_CB + "select_source_description" + PARAM_SEPARATOR; // + sourceId + page
    public static final String BUTTON_CB_SELECT_SOURCE_DESCRIPTION_NAVIGATE = BUTTON_CB + "select_source_description_navigate" + PARAM_SEPARATOR; // + page
    public static final String BUTTON_CB_SELECT_SOURCE_CONFIRM = BUTTON_CB + "select_source_confirm" + PARAM_SEPARATOR; // sourceId
    public static final String BUTTON_CB_SELECT_SOURCE_CONFIRM_NAVIGATE = BUTTON_CB + "select_source_confirm_navigate" + PARAM_SEPARATOR;
    public static final String BUTTON_T_SELECT_SOURCE_CONFIRM = "Seç";
    public static final String BUTTON_CB_SELECT_CITY = BUTTON_CB + "select_city" + PARAM_SEPARATOR; // sourceId
    public static final String BUTTON_CB_SELECT_CITY_NAVIGATE = BUTTON_CB + "select_city_navigate" + PARAM_SEPARATOR; // sourceId + navigateValue + page


}
