package com.jproger.conferencetelegrambot.users;

import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.users.entities.User;
import com.jproger.conferencetelegrambot.users.mappers.UserMapper;
import com.jproger.conferencetelegrambot.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto createUser(@Nonnull UserDto userDto) {
        User newUser = User.builder()
                .firstName(userDto.getFirstName())
                .middleName(userDto.getMiddleName())
                .lastName(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        User user = userRepository.save(newUser);

        return userMapper.toUserDtoMap(user);
    }
}
