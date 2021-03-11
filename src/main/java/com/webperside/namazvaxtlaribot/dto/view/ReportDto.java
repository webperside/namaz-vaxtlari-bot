package com.webperside.namazvaxtlaribot.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.IntStream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private Report_ActionLogDto success;
    private Report_ActionLogDto failed;
    private Report_ActionLogDto commandVaxtlar;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Report_ActionLogDto{
        private Integer uniqueUsersCount;
        private Integer used;

        public static Report_ActionLogDto fromIntegerList(List<Integer> list){
            return Report_ActionLogDto.builder()
                    .uniqueUsersCount(list.size())
                    .used(list.stream()
                            .mapToInt(Integer::intValue)
                            .sum()
                    )
                    .build();
        }
    }
}
