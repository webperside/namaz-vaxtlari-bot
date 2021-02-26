package com.webperside.namazvaxtlaribot.service.impl;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.dto.view.SendMessageDto;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;
import com.webperside.namazvaxtlaribot.dto.view.UserTelegramInfoDto;
import com.webperside.namazvaxtlaribot.enums.UserStatus;
import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.repository.UserRepository;
import com.webperside.namazvaxtlaribot.service.UserService;
import com.webperside.namazvaxtlaribot.telegram.TelegramHelper;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public Page<User> getAll(Integer page, String[] sortParams) {
        Sort sort = null;
        Sort.Direction direction = Sort.Direction.ASC;;
        if(isSortable(sortParams[0])){
            try{
                direction = Sort.Direction.fromString(sortParams[1]);
            } catch (Exception ignored){
            } finally {
                sort = Sort.by(direction, sortParams[0]);
            }
        } else {
            sort = Sort.unsorted();
        }

        Pageable pageable = PageRequest.of(page, 10, sort);
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
        executor.sendText(Long.parseLong(dto.getUserTgId()),dto.getMessage());
    }

    private boolean isSortable(String param){
        List<String> params = Arrays.asList(
                "createdAt","userStatus"
        );
        return params.contains(param);
    }
}
