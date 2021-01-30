package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.models.City;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CityService {

    Page<City> getAllBySourceId(Integer sourceId, Integer page);

    City getCityById(Integer cityId);
}
