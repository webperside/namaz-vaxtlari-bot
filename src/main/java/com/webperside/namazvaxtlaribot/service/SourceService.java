package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.models.Source;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SourceService {

    Page<Source> getAll(Integer page);
}
