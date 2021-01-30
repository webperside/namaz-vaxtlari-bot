package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.models.CitySettlement;
import com.webperside.namazvaxtlaribot.repository.CitySettlementRepository;
import com.webperside.namazvaxtlaribot.service.CitySettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CitySettlementServiceImpl implements CitySettlementService {

    private final CitySettlementRepository citySettlementRepository;

    @Override
    public CitySettlement getById(Integer citySettlementId) {
        return citySettlementRepository.findById(citySettlementId).orElseThrow(EntityNotFoundException::new);
    }
}
