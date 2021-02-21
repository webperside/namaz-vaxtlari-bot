package com.webperside.namazvaxtlaribot.dto.view;

import com.webperside.namazvaxtlaribot.models.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceDto {
    private Integer id;
    private String name;
    private String description;
    private String url;

    public static SourceDto fromEntity(Source source){
        return SourceDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .url(source.getUrl())
                .build();
    }

    public static Source toEntity(SourceDto dto){
        return Source.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .url(dto.getUrl())
                .build();
    }
}
