package com.Urna.Entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Apuracao {
    private Integer totalVotos;
    private List<Candidato> candidatosPrefeito;
    private List<Candidato> candidatosVereador;

}
