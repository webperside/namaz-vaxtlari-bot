package com.webperside.namazvaxtlaribot.repository;

import com.webperside.namazvaxtlaribot.models.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, Integer> {
}
