package com.projeto.ms_transacoes.controller;

import com.projeto.ms_transacoes.service.TransacaoService;
import com.projeto.ms_transacoes.domain.Transacao;
import com.projeto.ms_transacoes.dto.TransferenciaRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<Transacao> realizarTransferencia(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO){
        Transacao transacao = transacaoService.realizarTransferencia(transferenciaRequestDTO);

        return ResponseEntity.ok(transacao);
    }
}
