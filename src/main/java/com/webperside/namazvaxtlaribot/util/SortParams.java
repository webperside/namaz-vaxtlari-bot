package com.webperside.namazvaxtlaribot.util;

import com.webperside.namazvaxtlaribot.models.User;

import java.util.HashMap;
import java.util.Map;

public class SortParams {

    public static Map<String, String> split(String param){
        String[] params = param.split(",");
        Map<String, String> map = new HashMap<>();

        for(String s : params){
            String key = s.split(":")[0];
            String value = s.split(":")[1];
            map.put(key, value);
        }

        return map;
    }

    public static String fieldName(String[] sortParams){
        if(User.Fields.userStatus.equals(sortParams[0])){
            return "First " + (sortParams[1].equals("asc") ? "Stopped" : "Active") + " users";
        } else if("createdAt".equals(sortParams[0])){ // common field
            return (sortParams[1].equals("asc") ? "Oldest" : "Last") + " created";
        }
        return "Sort";
    }
}
