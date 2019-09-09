package com.jproger.conferencetelegrambot.users;

import com.jproger.conferencetelegrambot.common.dto.PageRequestDto;
import com.jproger.conferencetelegrambot.common.dto.PageResponseDto;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.users.entities.User;
import com.jproger.conferencetelegrambot.users.mappers.UserMapper;
import com.jproger.conferencetelegrambot.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto createUser(@Nonnull UserDto userDto) {
        User user = userRepository.save(
                User.builder()
                        .firstName(userDto.getFirstName())
                        .middleName(userDto.getMiddleName())
                        .lastName(userDto.getLastName())
                        .phoneNumber(userDto.getPhoneNumber())
                        .build()
        );

        return userMapper.toUserDto(user);
    }

    public PageResponseDto<UserDto> getUsers(@Valid PageRequestDto page) {
        Page<UserDto> users = userRepository.findAll(PageRequest.of(page.getPage(), page.getSize()))
                .map(userMapper::toUserDto);

        return PageResponseDto.of(users);
    }

    public Optional<UserDto> getUserById(long userId) {
        return userRepository.findById(userId)
                .map(userMapper::toUserDto);
    }
}
