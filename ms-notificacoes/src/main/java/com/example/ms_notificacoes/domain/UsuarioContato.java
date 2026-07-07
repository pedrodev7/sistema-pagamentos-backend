package com.example.ms_notificacoes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_contato")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioContato {
    @Id
    private Long id;
    private String name;
    private String email;
}
