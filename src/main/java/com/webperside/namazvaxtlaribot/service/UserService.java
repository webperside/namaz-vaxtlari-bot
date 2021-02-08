package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.dto.rest.TelegramUserByIdDto;
import com.webperside.namazvaxtlaribot.models.User;

import java.util.List;

public interface UserService {

    User getById(Integer id);

    User getByTgId(String tgId);

    TelegramUserByIdDto getUserInfoByTgIdFromTelegram(String userTgId);

    List<User> getAll();

    boolean existsByTgId(String tgId);

    void save(String tgId);

    void update(User user);
}
