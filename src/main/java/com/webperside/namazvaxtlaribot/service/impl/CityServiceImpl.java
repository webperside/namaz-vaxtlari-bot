package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.models.City;
import com.webperside.namazvaxtlaribot.repository.CityRepository;
import com.webperside.namazvaxtlaribot.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public Page<City> getAllBySourceId(Integer sourceId, Integer page) {
        return cityRepository.findAllBySourceId(sourceId, PageRequest.of(page, 8));
    }

    @Override
    public City getCityById(Integer cityId) {
        return cityRepository.findById(cityId).orElseThrow(EntityNotFoundException::new);
    }
}