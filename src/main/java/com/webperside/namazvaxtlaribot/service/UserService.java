package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.models.User;

import java.util.List;

public interface UserService {

    User getById(Integer id);

    User getByTgId(String tgId);

    List<User> getAll();

    List<UserDto> getAllWithTelegramInfo(Integer page);

    boolean existsByTgId(String tgId);

    void save(String tgId);

    void update(User user);
}
