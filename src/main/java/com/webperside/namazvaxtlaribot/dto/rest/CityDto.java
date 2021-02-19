package com.webperside.namazvaxtlaribot.dto.rest;

import com.webperside.namazvaxtlaribot.models.City;
import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.models.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {

    private Integer id;
    private String name;
    private String value;
    private Integer sourceId;
    private List<CityDto_Settlement> settlements;

    public static CityDto fromEntity(City city){
        List<Settlement> ss = city.getSettlements();
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .value(city.getValue())
                .sourceId(city.getSource().getId())
                .settlements(ss.stream()
                        .map( s -> CityDto_Settlement.builder()
                                .id(s.getId())
                                .name(s.getName())
                                .value(s.getValue())
                                .build()
                        ).collect(Collectors.toList()))
                .build();
    }

    public static City toEntity(CityDto dto){
        Source source = new Source();
        source.setId(dto.getSourceId());

        City city = City.builder()
                .id(dto.getId())
                .name(dto.getName())
                .source(source)
                .value(dto.getValue())
                .build();

        List<Settlement> settlements = new ArrayList<>();
        dto.settlements.forEach(set -> {
            settlements.add(
                    Settlement.builder()
                            .id(set.getId())
                            .name(set.getName())
                            .value(set.getValue())
                            .city(city)
                            .build()
            );
        });

        city.setSettlements(settlements);
        return city;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CityDto_Settlement{
        private Integer id;
        private String name;
        private String value;
    }
}
