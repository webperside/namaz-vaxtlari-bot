package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.dto.view.SendMessageDto;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.repository.UserRepository;
import com.webperside.namazvaxtlaribot.service.UserService;
import com.webperside.namazvaxtlaribot.telegram.TelegramHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TelegramHelper.Executor executor;

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByTgId(String tgId) {
        return userRepository.findByUserTgId(tgId).orElse(null);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDto> getAllWithTelegramInfo(Integer page) {
        List<UserDto> userDtoList = new ArrayList<>();
        List<User> users = getAll();
        users.forEach(user -> {
            UserDto userDto = executor.getUserInfoDetail(Long.parseLong(user.getUserTgId()));
            userDto.setId(user.getId());

            Settlement settlement = user.getSettlement();
            userDto.setSettlement(
                    UserDto.UserDto_Settlement.builder()
                            .id(settlement == null ? Integer.valueOf(0) : settlement.getId())
                            .name(settlement == null ? "n/a" : settlement.getName())
                            .build()
            );

            userDtoList.add(userDto);
        });
        return userDtoList;
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
//        Constants.users = getAll(); // update stored user data
    }

    @Override
    public void sendCustomMessage(SendMessageDto dto) {
        executor.sendText(Long.parseLong(dto.getUserTgId()),dto.getMessage());
    }
}
