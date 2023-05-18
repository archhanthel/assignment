package com.speer.assignment.service;

import com.speer.assignment.dto.LoginRequest;
import com.speer.assignment.dto.SignupRequest;
import com.speer.assignment.dto.UserDto;
import com.speer.assignment.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public void signup(SignupRequest signupRequest) {
        UserDto userDto = new UserDto();
        userDto.setUsername(signupRequest.getUsername());
        userDto.setPassword(signupRequest.getPassword());

        userService.registerUser(userDto);
    }

    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }
}
