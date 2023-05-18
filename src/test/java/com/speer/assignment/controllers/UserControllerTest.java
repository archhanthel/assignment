package com.speer.assignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speer.assignment.dto.UserDto;
import com.speer.assignment.entity.User;
import com.speer.assignment.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void registerUser_ShouldReturnRegisteredUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("john_doe");

        User registeredUser = new User();
        registeredUser.setId("1");
        registeredUser.setUsername(userDto.getUsername());

        when(userService.registerUser(any(UserDto.class))).thenReturn(registeredUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(registeredUser.getId()))
                .andExpect(jsonPath("$.username").value(registeredUser.getUsername()))
                .andReturn();

        verify(userService, times(1)).registerUser(any(UserDto.class));
    }

    @Test
    public void getUserById_ShouldReturnUser() throws Exception {
        String userId = "1";
        User user = new User();
        user.setId(userId);
        user.setUsername("john_doe");

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andReturn();

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void getUserById_ShouldReturnNotFound_WhenUserNotFound() throws Exception {
        String userId = "1";

        when(userService.getUserById(userId)).thenReturn(null);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void updateUser_ShouldReturnUpdatedUser() throws Exception {
        String userId = "1";
        UserDto userDto = new UserDto();
        userDto.setUsername("john_smith");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername(userDto.getUsername());

        when(userService.updateUser(userId, userDto)).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()))
                .andReturn();

        verify(userService, times(1)).updateUser(userId, userDto);
    }

    @Test
    public void deleteUser_ShouldReturnNoContent() throws Exception {
        String userId = "1";

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(userService, times(1)).deleteUser(userId);
    }

    private String asJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
