package com.speer.assignment;

import com.speer.assignment.dto.LoginDto;
import com.speer.assignment.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/api/auth/signup")
    public void signUp(@RequestBody UserDto userDto) {
    }

    @PostMapping("/api/auth/login")
    public String login(@RequestBody LoginDto loginDto) {
        return "YOUR_ACCESS_TOKEN";
    }
}
