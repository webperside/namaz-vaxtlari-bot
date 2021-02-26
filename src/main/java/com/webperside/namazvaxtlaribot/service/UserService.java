package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.dto.view.SendMessageDto;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.dto.view.UserTelegramInfoDto;
import com.webperside.namazvaxtlaribot.models.User;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;

public interface UserService {

    User getById(Integer id);

    User getByTgId(String tgId);

    Page<User> getAll(Integer page, String[] sortParams);

    Page<UserDto> getAllWithInfo(Integer page, String[] sortParams);

    UserTelegramInfoDto getTelegramInfoByUserId(String tgId);

    boolean existsByTgId(String tgId);

    void save(String tgId);

    void update(User user);

    void sendCustomMessage(SendMessageDto dto);
}
