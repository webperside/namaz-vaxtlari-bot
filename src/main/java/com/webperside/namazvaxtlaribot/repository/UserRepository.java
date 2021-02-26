package com.webperside.namazvaxtlaribot.repository;

import com.webperside.namazvaxtlaribot.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUserTgId(String userTgId);

    Optional<User> findByUserTgId(String userTgId);
}
