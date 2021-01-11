package com.webperside.namazvaxtlaribot.repository;

import com.webperside.namazvaxtlaribot.models.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement,Integer> {
}
