package com.webperside.namazvaxtlaribot.repository;

import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUserTgId(String userTgId);

    Optional<User> findByUserTgId(String userTgId);

    @Query(value="select u.id from User u where u.userTgId = ?1")
    Integer findUserIdByUserTgId(String userTgId);

    List<User> findAllBySettlementIsNull();
}
