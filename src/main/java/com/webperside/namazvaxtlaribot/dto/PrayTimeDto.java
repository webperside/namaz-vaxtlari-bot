package com.webperside.namazvaxtlaribot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrayTimeDto {

    private static final String IMSAK = "imsak";
    private static final String GUNES = "gunes";
    private static final String OGLE = "ogle";
    private static final String IKINDI = "ikindi";
    private static final String AKSAM = "aksam";
    private static final String YATSI = "yatsi";

    private LocalDateTime imsak;
    private LocalDateTime subh;
    private LocalDateTime gunChixir;
    private LocalDateTime zohr;
    private LocalDateTime esr;
    private LocalDateTime gunBatir;
    private LocalDateTime megrib;
    private LocalDateTime isha;
    private LocalDateTime geceYarisi;

    public void addForNamazZamaniNet(String key, String value){

        LocalDateTime ldt = prepare(value);
        switch (key){
            case IMSAK:{ setImsak(ldt); break; }
            case GUNES:{ setGunChixir(ldt); break; }
            case OGLE:{ setZohr(ldt); break; }
            case IKINDI:{ setEsr(ldt); break; }
            case AKSAM:{ setMegrib(ldt); break; }
            case YATSI:{ setIsha(ldt); break; }
        }
    }

    public void addForAhlibeytAz(Integer index, String value){
        LocalDateTime ldt = prepare(value);

        switch (index){
            case 1: {setImsak(ldt); break;}
            case 2: {setSubh(ldt); break;}
            case 3: {setGunChixir(ldt); break;}
            case 4: {setZohr(ldt); break;}
            case 5: {setEsr(ldt); break;}
            case 6: {setGunBatir(ldt); break;}
            case 7: {setMegrib(ldt); break;}
            case 8: {setIsha(ldt); break;}
            case 9: {setGeceYarisi(ldt); break;}
        }
    }

    public PrayTimeDto changeByValue(String value){
        return PrayTimeDto.builder()
                .imsak(prepare(getImsak(), value))
                .subh(prepare(getSubh(), value))
                .gunChixir(prepare(getGunChixir(),value))
                .zohr(prepare(getZohr(), value))
                .esr(prepare(getEsr(), value))
                .gunBatir(prepare(getGunBatir(), value))
                .megrib(prepare(getMegrib(), value))
                .isha(prepare(getIsha(), value))
                .geceYarisi(prepare(getGeceYarisi(), value))
                .build();
    }

    private LocalDateTime prepare(String value){
        String[] hourMinute = value.split(":");

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourMinute[0]));
        now.set(Calendar.MINUTE, Integer.parseInt(hourMinute[1]));
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        return LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
    }

    private LocalDateTime prepare(LocalDateTime ldt, String value){
        return ldt.plusMinutes(Long.parseLong(value));
    }
}
