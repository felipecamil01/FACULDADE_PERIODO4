package com.Urna.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
enum StatusEleitor {
    APTO, INATIVO, BLOQUEADO, PENDENTE, VOTOU;
}
