package com.jproger.conferencetelegrambot.users.mappers;

import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.users.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDto toUserDtoMap(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public List<UserDto> toUserDtoMap(List<User> users) {
        return users.stream()
                .map(this::toUserDtoMap)
                .collect(Collectors.toList());
    }
}
