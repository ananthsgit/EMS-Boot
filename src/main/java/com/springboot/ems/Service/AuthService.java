package com.springboot.ems.Service;

import com.springboot.ems.model.User;

public interface AuthService {
    void register(User user);
    String login(User user);
}
