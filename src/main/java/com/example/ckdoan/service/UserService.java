package com.example.ckdoan.service;

import com.example.ckdoan.dto.UserDto;
import com.example.ckdoan.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
