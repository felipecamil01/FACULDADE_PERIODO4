package com.Urna.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor

    @Entity
    public class Voto {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        private LocalDateTime dataHora;

        @NotNull
        @ManyToOne
        @JoinColumn(name = "candidato_prefeito_id")
        private Candidato candidatoPrefeito;

        @NotNull
        @ManyToOne
        @JoinColumn(name = "candidato_vereador_id")
        private Candidato candidatoVereador;

        private String hashComprovante;

    }
