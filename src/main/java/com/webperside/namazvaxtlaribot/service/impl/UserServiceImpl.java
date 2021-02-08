package com.webperside.namazvaxtlaribot.service.impl;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.response.GetChatResponse;
import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.dto.rest.TelegramUserByIdDto;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.repository.UserRepository;
import com.webperside.namazvaxtlaribot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.webperside.namazvaxtlaribot.telegram.TelegramConfig.execute;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByTgId(String tgId) {
        return userRepository.findByUserTgId(tgId).orElse(null);
    }

    @Override
    public TelegramUserByIdDto getUserInfoByTgIdFromTelegram(String userTgId) {
        GetChatResponse response = execute(new GetChat(userTgId));
        Chat chat = response.chat();
        return TelegramUserByIdDto.builder()
                .id(userTgId)
                .firstName(chat.firstName())
                .lastName(chat.lastName())
                .username(chat.username())
                .build();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByTgId(String tgId) {
        return userRepository.existsByUserTgId(tgId);
    }

    @Override
    public void save(String tgId) {
        userRepository.save(User.builder().userTgId(tgId).build());
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
        Constants.users = getAll(); // update stored user data
    }
}
