package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.dto.view.SendMessageDto;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.dto.view.UserTelegramInfoDto;
import com.webperside.namazvaxtlaribot.models.User;

import java.util.List;

public interface UserService {

    User getById(Integer id);

    User getByTgId(String tgId);

    List<User> getAll();

    List<UserDto> getAllWithInfo(Integer page);

    UserTelegramInfoDto getTelegramInfoByUserId(String tgId);

    boolean existsByTgId(String tgId);

    void save(String tgId);

    void update(User user);

    void sendCustomMessage(SendMessageDto dto);
}
