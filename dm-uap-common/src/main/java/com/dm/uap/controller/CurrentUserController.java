package com.dm.uap.controller;

import com.dm.common.exception.DataNotExistException;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.converter.UserConverter;
import com.dm.uap.dto.UserDto;
import com.dm.uap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/current")
@RequiredArgsConstructor
public class CurrentUserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @GetMapping
    public UserDto current(@AuthenticationPrincipal UserDetailsDto user) {
        return userService.get(user.getId()).map(userConverter::toDto).orElseThrow(DataNotExistException::new);
    }

    @PutMapping
    public UserDto update(@AuthenticationPrincipal(expression = "id") Long userId, UserDto user) {
        return null;
    }
}
