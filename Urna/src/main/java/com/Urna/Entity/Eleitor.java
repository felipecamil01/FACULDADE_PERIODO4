package com.Urna.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class Eleitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;
    @CPF
    private  String cpf;

    @NotBlank
    private String profissao;

    @Pattern(regexp="^\\(\\d{2}\\) \\d{5}-\\d{4}$", message="Celular inválido")
    private String telefoneCelular;

    private String telefoneFixo;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private StatusEleitor status;


}
