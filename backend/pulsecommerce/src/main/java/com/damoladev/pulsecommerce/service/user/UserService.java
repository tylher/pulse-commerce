package com.damoladev.pulsecommerce.service.user;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.UserCreationDto;

public interface UserService {
    public ApiResponseDto createUser(UserCreationDto dto);
    public ApiResponseDto getUser(String id);
    public ApiResponseDto getAllUsers();
}
