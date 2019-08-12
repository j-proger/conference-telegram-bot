package com.jproger.conferencetelegrambot.users;

import com.jproger.conferencetelegrambot.users.dto.PageRequestDto;
import com.jproger.conferencetelegrambot.users.dto.PageResponseDto;
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
        User newUser = User.builder()
                .firstName(userDto.getFirstName())
                .middleName(userDto.getMiddleName())
                .lastName(userDto.getLastName())
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        User user = userRepository.save(newUser);

        return userMapper.toUserDtoMap(user);
    }

    public PageResponseDto<UserDto> getUsers(@Valid PageRequestDto page) {
        Page<User> users = userRepository.findAll(PageRequest.of(page.getPage(), page.getSize()));

        return PageResponseDto.<UserDto>builder()
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .currentPage(users.getNumber())
                .elements(
                        userMapper.toUserDtoMap(users.getContent())
                )
                .build();
    }

    public Optional<UserDto> getUserById(long userId) {
        return userRepository.findById(userId)
                .map(userMapper::toUserDtoMap);
    }
}
