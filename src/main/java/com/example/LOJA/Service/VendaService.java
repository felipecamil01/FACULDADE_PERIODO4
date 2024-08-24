package com.example.LOJA.Service;

import com.example.LOJA.Entity.Venda;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.LOJA.repository.VendaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VendaService {
    @Autowired
    private VendaRepository vendaRepository;

    public Venda salvarVenda(Venda venda){

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
