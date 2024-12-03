package org.example.smartresidenceactivity.service;

import lombok.RequiredArgsConstructor;
import org.example.smartresidenceactivity.model.reqest.UserRequest;
import org.example.smartresidenceactivity.model.response.UserResponse;
import org.example.smartresidenceactivity.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Transactional
    public UserResponse createUser(UserRequest user) {
        return null;
    }
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        return null;
    }
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return null;
    }
    @Transactional
    public UserResponse updateUser(UUID id, UserRequest user) {
        return null;
    }
    @Transactional
    public void deleteUser(UUID id) {
    }
}
