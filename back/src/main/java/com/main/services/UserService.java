package com.main.services;

import com.main.models.User;
import com.main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * service for interact with mongodb repository
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    // repositories
    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * get all user
     * @return list of all user
     */
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    /**
     * get User by id
     * @param userId : user's id to retrieve
     * @return one user
     */
    public Optional<User> getUser(String userId) {
        return this.userRepository.findById(userId);
    }
}
