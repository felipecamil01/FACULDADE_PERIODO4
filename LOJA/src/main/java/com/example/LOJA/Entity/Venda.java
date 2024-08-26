package com.example.LOJA.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String enderecoEntrega;

    @DecimalMin("0.01")
    private BigDecimal valorTotal;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "venda_produto",
            joinColumns = @JoinColumn(name = "venda_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )

    private List<Produto> produtos;

    @NotNull
    private LocalDate dataVenda;


}