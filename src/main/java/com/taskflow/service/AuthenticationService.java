package com.taskflow.service;

import com.taskflow.domain.dto.response.auth.JwtAuthenticationResponseDto;
import com.taskflow.domain.entity.User;
import com.taskflow.exception.customexceptions.BadRequestException;
import com.taskflow.exception.customexceptions.ValidationException;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    JwtAuthenticationResponseDto signup(User user, String organizationId) throws ValidationException;
    JwtAuthenticationResponseDto signin(User user) throws BadRequestException;
}
