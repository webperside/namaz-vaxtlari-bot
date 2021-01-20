package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.models.Source;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SourceService {

    Page<Source> getAll(Integer page);

    Optional<Source> findById(Integer sourceId);
}
