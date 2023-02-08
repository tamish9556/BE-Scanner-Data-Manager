package com.dashboard.service;

import com.dashboard.model.UserModel;
import com.dashboard.repository.UserRepo;
import com.dashboard.utils.Encryption;
import com.dashboard.controller.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;



@Service
@Slf4j
public class LogInService {

    @Autowired
    private UserRepo userRepo;

    public String logIn(String name, String password) throws UserNotFoundException , NoSuchAlgorithmException {
        log.info("login with username {} and password {}", name, password);
        String passwordEncryption = Encryption.encryption(password);
            UserModel user = userRepo.findByUserName(name);
            if (user != null && user.getPassword().equals(passwordEncryption)) {
                return getUUid();
            } else {
                throw new UserNotFoundException("NOT FOUND");
            }
        }



    public static String getUUid() {
        return UUID.randomUUID().toString();

    }
}
