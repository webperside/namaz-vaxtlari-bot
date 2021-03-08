package com.webperside.namazvaxtlaribot.enums;

public enum Month {

    UNDEFINED(0, "Undefined"),
    JANUARY(1, "Yanvar"),
    FEBRUARY(2, "Fevral"),
    MARCH(3, "Mart"),
    APRIL(4, "Aprel"),
    MAY(5, "May"),
    JUNE(6, "İyun"),
    JULY(7, "İyul"),
    AUGUST(8, "Avqust"),
    SEPTEMBER(9, "Sentyabr"),
    OCTOBER(10, "Oktyabr"),
    NOVEMBER(11, "Noyabr"),
    DECEMBER(12, "Dekabr");

    private final Integer month;
    private final String name;

    Month(Integer month, String name) {
        this.month = month;
        this.name = name;
    }

    public Integer getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }

    public static Month getNameByMonth(Integer month){
        for(Month m : values()){
            if(m.month.equals(month)){
                return m;
            }
        }
        return UNDEFINED;
    }
}
