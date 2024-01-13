package com.taskflow.web.rest;

import com.taskflow.config.context.TenantContext;
import com.taskflow.domain.mapper.UserMapper;
import com.taskflow.repository.UserRepository;
import com.taskflow.utils.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tenant")
@RequiredArgsConstructor
public class UserRest {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

//    @GetMapping("/all")
//    public ResponseEntity<Response<List<UserResponseDto>>> getAllUsers() {
//        Response<List<UserResponseDto>> response = new Response<>();
//        List<User> users = userRepository.findAll();
//        response.setResult(
//                users.stream()
//                        .map(userMapper::toDto)
//                        .toList()
//        );
//        response.setMessage("Users retrieved successfully");
//        return ResponseEntity.ok().body(response);
//    }

    @GetMapping
    public ResponseEntity<Response<String>> getUser() {
        Response<String> response = new Response<>();
        response.setResult(TenantContext.getCurrentTenant());
        return ResponseEntity.ok().body(response);
    }
}
