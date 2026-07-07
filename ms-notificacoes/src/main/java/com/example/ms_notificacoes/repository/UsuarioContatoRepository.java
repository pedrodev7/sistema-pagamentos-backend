package com.example.ms_notificacoes.repository;


import com.example.ms_notificacoes.domain.UsuarioContato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioContatoRepository extends JpaRepository<UsuarioContato, Long> {
}
