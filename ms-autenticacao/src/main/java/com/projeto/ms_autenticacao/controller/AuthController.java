package com.projeto.ms_autenticacao.controller;

import com.projeto.ms_autenticacao.domain.Usuario;
import com.projeto.ms_autenticacao.dto.LoginRequestDto;
import com.projeto.ms_autenticacao.dto.RegisterRequestDto;
import com.projeto.ms_autenticacao.dto.ResponseTokenDto;
import com.projeto.ms_autenticacao.mensaging.producer.UsuarioProducer;
import com.projeto.ms_autenticacao.repository.UsuarioRepository;
import com.projeto.ms_autenticacao.security.TokenService;
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
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UsuarioProducer usuarioProducer;


    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService, UsuarioProducer usuarioProducer) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.usuarioProducer = usuarioProducer;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDto.email()).orElseThrow(() -> new RuntimeException("Usuario nãol encontrado"));
        if(passwordEncoder.matches(loginRequestDto.senha(), usuario.getSenha())){
            String token = this.tokenService.gerarToken(usuario);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseTokenDto(token));
        }

        return ResponseEntity.badRequest().build();

    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegisterRequestDto registerRequestDto){
        Optional<Usuario> findUsuario = usuarioRepository.findByEmail(registerRequestDto.email());

        if(findUsuario.isEmpty()){
            Usuario usuario = new Usuario();
            usuario.setSenha(passwordEncoder.encode(registerRequestDto.senha()));
            usuario.setNome(registerRequestDto.nome());
            usuario.setEmail(registerRequestDto.email());
            usuarioProducer.enviarMensagem(usuarioRepository.save(usuario), registerRequestDto);

            String token = this.tokenService.gerarToken(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseTokenDto(token));
        }

        return ResponseEntity.badRequest().build();

    }
}
