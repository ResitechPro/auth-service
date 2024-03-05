package com.taskflow.web.rest;

import com.taskflow.domain.dto.request.auth.SigninRequestDto;
import com.taskflow.domain.dto.request.auth.SignupRequestDto;
import com.taskflow.domain.dto.request.jwt.RefreshTokenRequestDto;
import com.taskflow.domain.dto.request.jwt.ValidateTokenRequestDto;
import com.taskflow.domain.dto.request.user.UserRequestDto;
import com.taskflow.domain.dto.response.auth.JwtAuthenticationResponseDto;
import com.taskflow.domain.dto.response.jwt.RefreshTokenResponseDTO;
import com.taskflow.domain.entity.RefreshToken;
import com.taskflow.domain.entity.User;
import com.taskflow.domain.mapper.UserMapper;
import com.taskflow.exception.customexceptions.BadRequestException;
import com.taskflow.exception.customexceptions.InValidRefreshTokenException;
import com.taskflow.exception.customexceptions.ValidationException;
import com.taskflow.service.AuthenticationService;
import com.taskflow.service.JwtService;
import com.taskflow.service.RefreshTokenService;
import com.taskflow.service.UserService;
import com.taskflow.utils.Response;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRest {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserMapper userMapper;
    public AuthRest(
        AuthenticationService authenticationService,
        RefreshTokenService refreshTokenService,
        JwtService jwtService,
        UserService userService,
        UserMapper userMapper
    ) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response<JwtAuthenticationResponseDto>> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) throws ValidationException {
        Response<JwtAuthenticationResponseDto> response = new Response<>();
        User user = userMapper.toUser(signupRequestDto);
        JwtAuthenticationResponseDto jwtAuthenticationResponseDto = authenticationService.signup(user,signupRequestDto.getOrganizationId());
        jwtAuthenticationResponseDto.setRefreshToken(
            refreshTokenService.getOrCreateRefreshToken(user.getEmail())
            .getToken()
        );
        response.setResult(jwtAuthenticationResponseDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Response<JwtAuthenticationResponseDto>> signin(@RequestBody @Valid SigninRequestDto signinRequestDto) throws BadRequestException {
        Response<JwtAuthenticationResponseDto> response = new Response<>();
        User user = User.builder()
                .email(signinRequestDto.getEmail())
                .password(signinRequestDto.getPassword())
                .build();
        JwtAuthenticationResponseDto jwtAuthenticationResponseDto =  authenticationService.signin(user);
        jwtAuthenticationResponseDto.setRefreshToken(
                refreshTokenService.getOrCreateRefreshToken(user.getEmail())
                        .getToken()
        );
        response.setResult(jwtAuthenticationResponseDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponseDTO refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDTO) throws InValidRefreshTokenException {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken());
        RefreshToken refreshToken = null;
        if (optionalRefreshToken.isEmpty())
            refreshTokenService.throwInValidRefreshTokenException("invalid refresh token.");
        else {
            refreshToken = optionalRefreshToken.get();
            if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
                refreshTokenService.delete(refreshToken);
                refreshTokenService.throwInValidRefreshTokenException("Refresh token was expired. Please make a new signin.");
            }
        }
        UserDetails userDetails = userService.getUserIfExitOrThrowException(refreshToken.getUser().getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        return RefreshTokenResponseDTO.builder()
                .accessToken(accessToken)
                .build();
    }


    @PostMapping("/validate-token")
    public ResponseEntity<Response<Boolean>> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {
        Response<Boolean> response = new Response<>();
        String username = jwtService.extractUserName(validateTokenRequestDto.getAccessToken());
        boolean validated = false;
        if(StringUtils.isNotEmpty(username)){
            UserDetails userDetails = userService.userDetailsService()
                    .loadUserByUsername(username);
            if (jwtService.isTokenValid(validateTokenRequestDto.getAccessToken(), userDetails)) validated = true;
        }
        response.setResult(validated);
        return ResponseEntity.ok().body(response);
    }
}
