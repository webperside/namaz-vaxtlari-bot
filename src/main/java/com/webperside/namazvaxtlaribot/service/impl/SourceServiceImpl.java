package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.repository.SourceRepository;
import com.webperside.namazvaxtlaribot.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    @Override
    public Page<Source> getAll(Integer page) {
        return sourceRepository.findAll(PageRequest.of(page, 4));
    }

    @Override
    public Optional<Source> findById(Integer sourceId) {
        return sourceRepository.findById(sourceId);
    }

    @Override
    public Source findByName(String name) {
        return sourceRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }
}
