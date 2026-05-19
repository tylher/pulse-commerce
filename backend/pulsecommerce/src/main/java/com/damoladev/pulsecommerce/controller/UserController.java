package com.damoladev.pulsecommerce.controller;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.UserCreationDto;
import com.damoladev.pulsecommerce.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    public ResponseEntity<ApiResponseDto> CreateUser(@Valid @RequestBody UserCreationDto dto){
        ApiResponseDto responseDto = userService.createUser(dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
