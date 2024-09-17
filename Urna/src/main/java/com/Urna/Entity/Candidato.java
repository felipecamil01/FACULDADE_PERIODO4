package com.Urna.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Cpf é obrigatório")
    @CPF(message = "Cpf inválido")
    private String cpf;

    @NotBlank(message = "Numero é obrigatório")
    @Column(unique = true, nullable = false)
    private String numero;

    @NotNull(message = "Função é obrigatória")
    @Min(value = 1, message = "Função deve ser 1 (prefeito) ou 2 (vereador)")
    @Max(value = 2, message = "Função deve ser 1 (prefeito) ou 2 (vereador)")
    private Integer funcao;

    @Enumerated(EnumType.STRING)
    private StatusCandidato status;
    
    @Transient
    private Integer votosApurados;

}
