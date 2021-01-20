package com.webperside.namazvaxtlaribot.repository;

import com.webperside.namazvaxtlaribot.models.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City,Integer> {

    Page<City> findAllBySourceId(Integer sourceId, Pageable pageable);
}
