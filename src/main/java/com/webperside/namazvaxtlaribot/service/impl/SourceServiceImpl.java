package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.dto.rest.SourceDto;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.repository.SourceRepository;
import com.webperside.namazvaxtlaribot.service.SourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    @Override
    public Page<Source> getAll(Integer page) {
        return sourceRepository.findAll(PageRequest.of(page, 4));
    }

    @Override
    public List<SourceDto> getAllShortInfo(Integer page) {
        return sourceRepository.findAll().stream().map(source -> SourceDto.builder().id(source.getId()).name(source.getName()).build()).collect(Collectors.toList());
    }

    @Override
    public Source findById(Integer sourceId) {
        return sourceRepository.findById(sourceId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Source findByName(String name) {
        return sourceRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(Source source) {
        sourceRepository.save(source);
    }
}
