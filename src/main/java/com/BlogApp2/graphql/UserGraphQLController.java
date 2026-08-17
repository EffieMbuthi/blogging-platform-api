package com.BlogApp2.graphql;

import com.BlogApp2.dto.request.UserRequest;
import com.BlogApp2.dto.response.UserResponse;
import com.BlogApp2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Validated
public class UserGraphQLController {

    private final UserService userService;

    @QueryMapping
    public UserResponse getUser(@Argument UUID id) {
        return userService.getUserById(id);
    }

    @QueryMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @MutationMapping
    public UserResponse createUser(@Argument @Valid UserRequest input) {
        return userService.createUser(input);
    }

    @MutationMapping
    public UserResponse updateUser(@Argument UUID id, @Argument @Valid UserRequest input) {
        return userService.updateUser(id, input);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument UUID id) {
        userService.deleteUser(id);
        return true;
    }
}
