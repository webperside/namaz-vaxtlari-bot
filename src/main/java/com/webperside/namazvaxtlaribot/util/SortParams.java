package com.webperside.namazvaxtlaribot.util;

import com.webperside.namazvaxtlaribot.models.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public static Pageable createRequest(Integer page, Integer size, String[] sortParams){
        Sort sort = null;
        Sort.Direction direction = Sort.Direction.ASC;
        if(isSortable(sortParams[0])){
            try{
                direction = Sort.Direction.fromString(sortParams[1]);
            } catch (Exception ignored){
            } finally {
                sort = Sort.by(direction, sortParams[0]);
            }
        } else {
            sort = Sort.unsorted();
        }

        return PageRequest.of(page, size, sort);
    }

    private static boolean isSortable(String param){
        List<String> params = Arrays.asList(
                "createdAt","userStatus","status"
        );
        return params.contains(param);
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
