package com.projeto.ms_transacoes.repository;

import com.projeto.ms_transacoes.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByUsuarioId(Long usuarioId);
}
