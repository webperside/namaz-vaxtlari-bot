package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.repository.UserRepository;
import com.webperside.namazvaxtlaribot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void save(String tgId) {
        userRepository.save(User.builder().userTgId(tgId).build());
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }
}
