package com.webperside.namazvaxtlaribot.util;

import java.util.HashMap;
import java.util.Map;

public class SearchParams {

    public static Map<String, String> prepare(String[] searchParams){
        Map<String, String> params = new HashMap<>();

        if(searchParams == null || searchParams.length == 0){
            return params;
        }

        for(String param : searchParams){
            String[] keyValue = param.split(":");
            params.put(keyValue[0], keyValue[1]);
        }

        return params;
    }
}
