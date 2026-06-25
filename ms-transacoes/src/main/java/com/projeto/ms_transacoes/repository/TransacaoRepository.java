package com.projeto.ms_transacoes.repository;

import com.projeto.ms_transacoes.domain.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

}
