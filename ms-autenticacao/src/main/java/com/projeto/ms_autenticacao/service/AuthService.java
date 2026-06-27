package com.projeto.ms_autenticacao.service;

import com.projeto.ms_autenticacao.domain.Usuario;
import com.projeto.ms_autenticacao.dto.LoginRequestDto;
import com.projeto.ms_autenticacao.dto.RegisterRequestDto;
import com.projeto.ms_autenticacao.dto.ResponseTokenDto;
import com.projeto.ms_autenticacao.repository.UsuarioRepository;
import com.projeto.ms_autenticacao.security.TokenService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
        return new User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
    }

    public ResponseTokenDto login(LoginRequestDto loginRequestDto) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDto.email()).orElseThrow(() -> new UsernameNotFoundException("Login ou Senha inválidos"));
        if(passwordEncoder.matches(loginRequestDto.senha(), usuario.getSenha())) {
            String token = tokenService.gerarToken(usuario);
            return new ResponseTokenDto(token);
        }

        throw new UsernameNotFoundException("Login ou Senha inválidos");
    }

    public ResponseTokenDto register(RegisterRequestDto registerRequestDto) {
        Optional<Usuario> findUsuario = usuarioRepository.findByEmail(registerRequestDto.email());

        if(findUsuario.isEmpty()){
            Usuario usuario = new Usuario();
            usuario.setSenha(passwordEncoder.encode(registerRequestDto.senha()));
            usuario.setNome(registerRequestDto.nome());
            usuario.setEmail(registerRequestDto.email());
            usuarioRepository.save(usuario);

            String token = this.tokenService.gerarToken(usuario);

            return new ResponseTokenDto(token);
        }

        throw new RuntimeException("Usuario já existe");
    }
}
