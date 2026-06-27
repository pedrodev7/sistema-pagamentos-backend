package com.projeto.ms_autenticacao.service;

import com.projeto.ms_autenticacao.domain.Usuario;
import com.projeto.ms_autenticacao.dto.LoginRequestDto;
import com.projeto.ms_autenticacao.dto.RegisterRequestDto;
import com.projeto.ms_autenticacao.dto.ResponseTokenDto;
import com.projeto.ms_autenticacao.exception.EntityExistsException;
import com.projeto.ms_autenticacao.mensaging.producer.UsuarioProducer;
import com.projeto.ms_autenticacao.repository.UsuarioRepository;
import com.projeto.ms_autenticacao.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioProducer usuarioProducer;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService, AuthenticationManager authenticationManager, UsuarioProducer usuarioProducer) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.usuarioProducer = usuarioProducer;
    }


    public ResponseTokenDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.senha());
        Authentication auth = authenticationManager.authenticate(userNamePassword);
        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = tokenService.gerarToken(usuario);
        return new ResponseTokenDto(token);
    }

    public ResponseTokenDto register(RegisterRequestDto registerRequestDto) {
        if (usuarioRepository.existsByEmail(registerRequestDto.email())) {
            throw new EntityExistsException("Usuario já cadastrado com o email: " + registerRequestDto.email());
        }

        Usuario usuario = new Usuario();
        usuario.setSenha(passwordEncoder.encode(registerRequestDto.senha()));
        usuario.setNome(registerRequestDto.nome());
        usuario.setEmail(registerRequestDto.email());
        usuarioRepository.save(usuario);

        usuarioProducer.enviarMensagem(usuario, registerRequestDto);

        return new ResponseTokenDto(tokenService.gerarToken(usuario));

    }
}
