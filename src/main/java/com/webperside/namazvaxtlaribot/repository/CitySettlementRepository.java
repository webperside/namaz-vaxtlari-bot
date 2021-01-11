package com.webperside.namazvaxtlaribot.repository;

import com.webperside.namazvaxtlaribot.models.CitySettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitySettlementRepository extends JpaRepository<CitySettlement,Integer> {
}
