package com.example.LOJA.Service;

import com.example.LOJA.Entity.Cliente;
import com.example.LOJA.Entity.Funcionario;
import com.example.LOJA.Entity.Produto;
import com.example.LOJA.Entity.Venda;
import com.example.LOJA.repository.ClienteRepository;
import com.example.LOJA.repository.FuncionarioRepository;
import com.example.LOJA.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.LOJA.repository.VendaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendaService {
    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;
private static final int IDADE_MINIMA = 18;
    @Transactional
    public Venda salvarVenda(Venda venda) {
        venda.setDataVenda(LocalDate.now());
        Cliente cliente = clienteRepository.findById(venda.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        if (cliente.getIdade() < IDADE_MINIMA) {
            throw new IllegalArgumentException("Cliente deve ter pelo menos " + IDADE_MINIMA + " anos para realizar uma compra.");
        }
        Funcionario funcionario = funcionarioRepository.findById(venda.getFuncionario().getId())
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado"));

        List<Produto> produtos = venda.getProdutos().stream()
                .map(produto -> produtoRepository.findById(produto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado")))
                .collect(Collectors.toList());

        venda.setCliente(cliente);
        venda.setFuncionario(funcionario);
        venda.setProdutos(produtos);
        venda.setDataVenda(LocalDate.now());

        BigDecimal valorTotal = produtos.stream()
                .map(Produto::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        venda.setValorTotal(valorTotal);


        venda.setProdutos(produtos);

        return vendaRepository.save(venda);
    }

    public List<Venda> buscarTodasAsVendas() {
        return vendaRepository.findAll();
    }


    public Optional<Venda> buscarVendaPorId(Long id) {
        return vendaRepository.findById(id);
    }


    public Venda atualizarVenda(Long id, Venda vendaAtualizada) {
        Optional<Venda> vendaOptional = vendaRepository.findById(id);

        if (vendaOptional.isPresent()) {
            Venda vendaExistente = vendaOptional.get();

            vendaExistente.setEnderecoEntrega(vendaAtualizada.getEnderecoEntrega());
            vendaExistente.setValorTotal(vendaAtualizada.getValorTotal());
            vendaExistente.setCliente(vendaAtualizada.getCliente());
            vendaExistente.setFuncionario(vendaAtualizada.getFuncionario());
            vendaExistente.setProdutos(vendaAtualizada.getProdutos());
            return vendaRepository.save(vendaExistente);
        } else {
            throw new EntityNotFoundException("Venda não encontrada com o ID: " + id);
        }
    }


    public void excluirVenda(Long id) {
        vendaRepository.deleteById(id);
    }


}
