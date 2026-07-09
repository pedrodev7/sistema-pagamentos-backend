package com.example.ms_notificacoes.service;

import com.example.ms_notificacoes.dto.EmailServiceDto;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void enviarComprovante(EmailServiceDto emailServiceDto){
        //TODO: Implementar envio de e-mail real usando JavaMailSender ou outro serviço de e-mail

        System.out.println("\n=======================================================");
        System.out.println("ENVIANDO E-MAILS DE COMPROVANTE...");
        System.out.println("=======================================================");

        // E-mail para quem enviou o dinheiro
        System.out.println("Para: " + emailServiceDto.emailOrigem());
        System.out.println("Assunto: Transferência Realizada com Sucesso");
        System.out.println("Olá " + emailServiceDto.nomeOrigem() + ", sua transferência de R$ " + emailServiceDto.valor() + " para " + emailServiceDto.nomeDestino() + " foi concluída.");

        System.out.println("-------------------------------------------------------");

        // E-mail para quem recebeu o dinheiro
        System.out.println("Para: " + emailServiceDto.nomeDestino());
        System.out.println("Assunto: Você recebeu uma nova transferência!");
        System.out.println("Olá " + emailServiceDto.nomeDestino() + ", você acabou de receber R$ " + emailServiceDto.valor() + " de " + emailServiceDto.nomeOrigem() + ".");
        System.out.println("=======================================================\n");
    }
}
