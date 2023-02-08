package com.dashboard.controller;

import com.dashboard.controller.ifc.AuthApi;
import com.dashboard.model.UserModel;
import com.dashboard.service.LogInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class logInController implements AuthApi {
    @Autowired
    private LogInService logInService;
    @Override
    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody UserModel user) {
        try {
            return ResponseEntity.ok().body(logInService.logIn(user.getUserName(), user.getPassword()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (NoSuchAlgorithmException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}