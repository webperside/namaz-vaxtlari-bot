package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.models.City;
import org.springframework.data.domain.Page;

public interface CityService {

    Page<City> getAllBySourceId(Integer sourceId, Integer page);
}
