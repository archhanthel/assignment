package com.speer.assignment.service;

import com.speer.assignment.dto.UserDto;
import com.speer.assignment.entity.User;
import com.speer.assignment.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerUser_ValidUserDto_ReturnsRegisteredUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("john.doe");
        userDto.setPassword("password");

        User savedUser = new User();
        savedUser.setId("1");
        savedUser.setUsername(userDto.getUsername());
        savedUser.setPassword("encodedPassword");

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        User registeredUser = userService.registerUser(userDto);

        assertNotNull(registeredUser);
        assertEquals(savedUser.getId(), registeredUser.getId());
        assertEquals(savedUser.getUsername(), registeredUser.getUsername());

        verify(userRepository, times(1)).findByUsername(userDto.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerUser_DuplicateUsername_ThrowsIllegalArgumentException() {
        UserDto userDto = new UserDto();
        userDto.setUsername("john.doe");
        userDto.setPassword("password");

        when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(new User()));

        userService.registerUser(userDto);
    }

    @Test
    public void getUserById_ExistingId_ReturnsUser() {
        String userId = "1";
        User user = new User();
        user.setId(userId);
        user.setUsername("john.doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User retrievedUser = userService.getUserById(userId);

        assertNotNull(retrievedUser);
        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getUsername(), retrievedUser.getUsername());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void getUserById_NonExistingId_ReturnsNull() {
        String userId = "1";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User retrievedUser = userService.getUserById(userId);

        assertNull(retrievedUser);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void updateUser_ExistingIdAndValidUserDto_ReturnsUpdatedUser() {
        String userId = "1";
        UserDto userDto = new UserDto();
        userDto.setUsername("john.doe");
        userDto.setPassword("password");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("jane.doe");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername(userDto.getUsername());
        updatedUser.setPassword("encodedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        User result = userService.updateUser(userId, userDto);

        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(updatedUser.getUsername(), result.getUsername());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(userDto.getPassword());
    }

    @Test(expected = RuntimeException.class)
    public void updateUser_NonExistingId_ThrowsRuntimeException() {
        String userId = "1";
        UserDto userDto = new UserDto();
        userDto.setUsername("john.doe");
        userDto.setPassword("password");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.updateUser(userId, userDto);
    }

    @Test
    public void deleteUser_ExistingId_DeletesUser() {
        String userId = "1";

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
