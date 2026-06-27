package com.projeto.ms_autenticacao.controller;

import com.projeto.ms_autenticacao.domain.Usuario;
import com.projeto.ms_autenticacao.dto.LoginRequestDto;
import com.projeto.ms_autenticacao.dto.RegisterRequestDto;
import com.projeto.ms_autenticacao.dto.ResponseTokenDto;
import com.projeto.ms_autenticacao.mensaging.producer.UsuarioProducer;
import com.projeto.ms_autenticacao.repository.UsuarioRepository;
import com.projeto.ms_autenticacao.security.TokenService;
import com.projeto.ms_autenticacao.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequestDto));
    }
}
