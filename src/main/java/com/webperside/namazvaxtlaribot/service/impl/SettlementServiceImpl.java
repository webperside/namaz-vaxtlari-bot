package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.repository.SettlementRepository;
import com.webperside.namazvaxtlaribot.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRepository settlementRepository;
    @Override
    public Settlement getById(Integer id) {
        return settlementRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
