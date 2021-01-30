package com.webperside.namazvaxtlaribot.util;

import com.webperside.namazvaxtlaribot.enums.Emoji;

public class CommonUtil {

    public static int getPageByValue(String navigateTo, int page){
        if(navigateTo.equals(Emoji.LEFT_ARROW.getCallback()) && page != 0){
            page--;
        } else if(navigateTo.equals(Emoji.RIGHT_ARROW.getCallback())){
            page++;
        }

        return page;
    }

}
