package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.models.User;

import java.util.List;

public interface UserService {

    User getById(Integer id);

    User getByTgId(String tgId);

    List<User> getAll();

    boolean existsByTgId(String tgId);

    void save(String tgId);

    void update(User user);
}
