package com.Urna.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor

    @Entity
    public class Candidato {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        private String nomeCompleto;

        @NotBlank
       @CPF
        private String cpf;


        @Pattern(regexp="^\\(\\d{2}\\) \\d{5}-\\d{4}$", message="Celular inválido")
        private String numero;

        @NotNull
        private Integer funcao; // 1 para prefeito, 2 para vereador

        @Transient
        private Integer votosApurados; // Campo transiente, não será persistido

        @Enumerated(EnumType.STRING)
        private StatusCandidato status;

    }



