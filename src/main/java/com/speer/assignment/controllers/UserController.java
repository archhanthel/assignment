package com.speer.assignment.controllers;

import com.speer.assignment.dto.UserDto;
import com.speer.assignment.entity.User;
import com.speer.assignment.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        User registeredUser = userService.registerUser(userDto);
        UserDto registeredUserDto = convertToDto(registeredUser);
        return ResponseEntity.ok(registeredUserDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            UserDto userDto = convertToDto(user);
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        User updatedUser = userService.updateUser(id, userDto);
        UserDto updatedUserDto = convertToDto(updatedUser);
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());

        return userDto;
    }
}
