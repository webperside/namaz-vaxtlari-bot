package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.dto.view.SendMessageDto;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.dto.view.UserTelegramInfoDto;
import com.webperside.namazvaxtlaribot.enums.UserStatus;
import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.repository.UserRepository;
import com.webperside.namazvaxtlaribot.runnables.BulkMessageRunner;
import com.webperside.namazvaxtlaribot.service.UserService;
import com.webperside.namazvaxtlaribot.telegram.TelegramHelper;
import com.webperside.namazvaxtlaribot.util.SortParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TelegramHelper.Executor executor;

    @Override
    public Integer getUserIdByUserTgId(String userTgId) {
        return userRepository.findUserIdByUserTgId(userTgId);
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getByTgId(String tgId) {
        return userRepository.findByUserTgId(tgId).orElse(null);
    }

    @Override
    public Page<User> getAll(Integer page, String[] sortParams) {
        Pageable pageable = SortParams.createRequest(page, 10, sortParams);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<UserDto> getAllWithInfo(Integer page, String[] sortParams) {
        List<UserDto> userDtoList = new ArrayList<>();
        Page<User> users = getAll(page, sortParams);
        users.getContent().forEach(user -> {

            Settlement settlement = user.getSettlement();

            UserDto dto = UserDto.builder()
                    .id(user.getId())
                    .userTelegramId(user.getUserTgId())
                    .settlement(UserDto.UserDto_Settlement.builder()
                            .id(settlement == null ? Integer.valueOf(0) : settlement.getId())
                            .name(settlement == null ? "n/a" : settlement.getName())
                            .build())
                    .userStatus(user.getUserStatus().getValue())
                    .createdAt(user.getCreatedAt())
                    .build();

            userDtoList.add(dto);
        });
        return new PageImpl<>(userDtoList, users.getPageable(), users.getTotalElements());
    }

    @Override
    public UserTelegramInfoDto getTelegramInfoByUserId(String tgId) {
        return executor.getUserInfoDetail(Long.valueOf(tgId));
    }

    @Override
    public boolean existsByTgId(String tgId) {
        return userRepository.existsByUserTgId(tgId);
    }

    @Override
    public void save(String tgId) {
        userRepository.save(
                User.builder()
                        .userTgId(tgId)
                        .userStatus(UserStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
//        Constants.users = getAll(); // update stored user data
    }

    @Override
    public void sendCustomMessage(SendMessageDto dto) {
        if(dto.getUserTgId() == null || dto.getUserTgId().isEmpty()){
            sendAsync(dto);
        } else{
            executor.sendText(Long.parseLong(dto.getUserTgId()),dto.getMessage());
        }

    }

    private void sendAsync(SendMessageDto dto){
        final String type = dto.getBulkMessageType();
        List<User> users;

        switch (type) {
            case Constants.BULK_MESSAGE_ALL:
                users = userRepository.findAll();
                break;
            case Constants.BULK_MESSAGE_W_SET:
                users = userRepository.findAllBySettlementIsNull();
                break;
            case Constants.BULK_MESSAGE_TEST_ADMIN:
                users = Collections.singletonList(
                        userRepository.findByUserTgId(String.valueOf(Constants.ADMIN_TELEGRAM_ID)
                        ).orElse(null));
                break;
            default:
                users = new ArrayList<>();
        }

        BulkMessageRunner runner = new BulkMessageRunner(users, dto.getMessage(), executor);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(runner);
        executorService.shutdown();
    }
}
