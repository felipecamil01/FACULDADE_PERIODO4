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

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @CPF(message = "CPF inválido")
    private  String cpf;

    @NotBlank(message = "Profissão é obrigatório")
    private String profissao;

    @NotBlank(message = "Telefone celular é obrigatório")
    @Pattern(regexp = "^\\(\\\\d{2}\\)\\s?\\\\d{5}-\\\\d{4}$", message = "Telefone celular inválido")
    private String telefoneCelular;
    
    @Pattern(regexp = "^\\(\\\\d{2}\\)\\s?\\\\d{4}-\\\\d{4}$", message = "Telefone fixo inválido")
    private String telefoneFixo;

    @Email(message = "Endereço de email inválido")
    private String email;

    @Enumerated(EnumType.STRING)
    private StatusEleitor status;

}
