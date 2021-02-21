package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.dto.view.SourceDto;
import com.webperside.namazvaxtlaribot.models.Source;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SourceService {

    Page<Source> getAll(Integer page);

    List<SourceDto> getAllShortInfo(Integer page);

    Source findById(Integer sourceId);

    Source findByName(String name);

    void save(Source source);
}
