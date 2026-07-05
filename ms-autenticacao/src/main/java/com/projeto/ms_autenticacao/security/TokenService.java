package com.projeto.ms_autenticacao.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.projeto.ms_autenticacao.domain.Usuario;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token}")
    private String secret;

    public String gerarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("ms-autenticacao")
                    .withSubject(usuario.getId().toString())
                    .withExpiresAt(this.gerarTempoExpiracao())
                    .sign(algorithm);

        } catch (JWTCreationException e){
            throw new RuntimeException("Erro durante autenticação");
        }
    }

    public String validarToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("ms-autenticacao")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant gerarTempoExpiracao(){
        return LocalDateTime.now()
                .plusHours(1)
                .toInstant(ZoneOffset.of("-03:00"));
    }
}
