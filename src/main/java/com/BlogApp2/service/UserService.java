package com.BlogApp2.service;

import com.BlogApp2.dto.request.UserRequest;
import com.BlogApp2.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse getUserById(UUID id);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(UUID id, UserRequest request);
    void deleteUser(UUID id);
}
