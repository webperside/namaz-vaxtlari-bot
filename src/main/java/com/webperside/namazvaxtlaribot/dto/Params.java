package com.webperside.namazvaxtlaribot.dto;

import com.webperside.namazvaxtlaribot.config.Constants;

import java.util.*;

public class Params {

    private final String main;
    private final Map<String, String> values;

    private Params(String main) {
        this.main = main;
        this.values = new HashMap<>();
    }

    public String getMain() {
        return main;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public static Builder builderWith(String main){
        return new Builder(main);
    }

    public String join(){
        List<String> params = new ArrayList<>();
        params.add(main);
        for (Map.Entry<String,String> entry : values.entrySet()){
            params.add(entry.getKey() + "=" + entry.getValue());
        }
        return String.join(Constants.PARAM_SEPARATOR, params);
    }

    public static Params split(String par){
        String[] paramsA = par.split(Constants.PARAM_SEPARATOR);
        Builder builder = Params.builderWith(paramsA[0]);

        for(int i = 1 ; i < paramsA.length ; i++){
            builder = builder.put(paramsA[i]);
        }

        return builder.build();
    }

    @Override
    public String toString() {
        return "main value : " + main + "\n" +
                "values : " + values;
    }

    public static class Builder{

        private final Params params;

        private Builder(String main) {
            this.params = new Params(main);
        }

        private Builder(Builder builder){
            this.params = builder.params;
        }

        public Builder put(String key, String value){
            this.params.values.put(key, value);
            return this;
        }

        public Builder put(String pair){
            String[] keyValue = pair.split("=");
            this.params.values.put(keyValue[0], keyValue[1]);
            return this;
        }


        public Builder copy(){
            return new Builder(this);
        }

        public Params build(){
            return this.params;
        }
    }

}
