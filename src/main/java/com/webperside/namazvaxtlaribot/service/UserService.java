package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.models.User;

public interface UserService {

    User getById(Integer id);

    User getByTgId(String tgId);

    boolean existsByTgId(String tgId);

    void save(String tgId);

    void update(User user);
}
