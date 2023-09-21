package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.Filter.Token;
import com.example.streetfoodfinder.domain.form.LoginForm;
import com.example.streetfoodfinder.service.LogInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class LogInController {
    private final LogInService logInService;

    @PostMapping("/sign-in")
    public ResponseEntity<Token> signInMember(@RequestBody @Valid LoginForm loginForm) {

        Token token = logInService.login(loginForm.getEmail(), loginForm.getPw());
        return ResponseEntity.ok(token);
    }
}
