package com.dashboard.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

public interface LogInServiceInterface {
     ResponseEntity<String> logIn(String name, String password, HttpSession session) ;
     ResponseEntity<String> UpdateNumberOfTry(HttpSession session) ;
}

