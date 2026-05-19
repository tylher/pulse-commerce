package com.damoladev.pulsecommerce.service.user;

import com.damoladev.pulsecommerce.dto.ApiResponseDto;
import com.damoladev.pulsecommerce.dto.UserCreationDto;
import com.damoladev.pulsecommerce.dto.UserResponseDto;
import com.damoladev.pulsecommerce.enums.UserRole;
import com.damoladev.pulsecommerce.exception.ResourceNotFoundException;
import com.damoladev.pulsecommerce.model.User;
import com.damoladev.pulsecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public ApiResponseDto createUser(UserCreationDto dto) {
        if (userRepository.existsByEmailIgnoreCase(dto.email().toLowerCase())){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setRole(UserRole.CUSTOMER);
        user.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(user);

        return new ApiResponseDto(true,"User created successfully");

    }

    @Override
    public ApiResponseDto getUser(String id){
        User savedUser = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User","id",id)
        );

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();

        return new ApiResponseDto(true, "User fetched successfully", userResponseDto);
    }


    @Override
    public ApiResponseDto getAllUsers(){
        List<UserResponseDto> users = userRepository.findAll().stream().map(
                user -> {
                    return UserResponseDto.builder()
                            .email(user.getEmail())
                            .lastName(user.getLastName())
                            .firstName(user.getFirstName())
                            .build();
                }
        ).toList();

        return new ApiResponseDto(true,"Users fetched successfully", users);
    }
}
