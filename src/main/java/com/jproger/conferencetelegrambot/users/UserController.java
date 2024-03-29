package com.jproger.conferencetelegrambot.users;

import com.jproger.conferencetelegrambot.common.dto.PageRequestDto;
import com.jproger.conferencetelegrambot.common.dto.PageResponseDto;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ApiOperation("Получение списка пользователей. Постранично.")
    public PageResponseDto<UserDto> getUsers(PageRequestDto page) {
        return userService.getUsers(page);
    }
}
