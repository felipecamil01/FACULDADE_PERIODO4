package com.example.LOJA.Service;

import com.example.LOJA.Entity.Cliente;
import com.example.LOJA.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    private static int IDADE_MINIMA=18;
    public Cliente save(Cliente cliente) {
        if (cliente.getIdade() < IDADE_MINIMA) {
            throw new IllegalArgumentException("Cliente deve ter pelo menos " + IDADE_MINIMA + " anos para realizar uma compra.");
        }
        return clienteRepository.save(cliente);
    }

    public List<Cliente> buscarTodosCliente() {
        return clienteRepository.findAll();

    }


    public Optional<Cliente> clienteBuscarPorId (Long id) {
        return clienteRepository.findById(id);
    }


    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);

        if (clienteOptional.isPresent()) {
            Cliente clienteExistente = clienteOptional.get();
            if (clienteExistente.getIdade() < IDADE_MINIMA) {
                throw new IllegalArgumentException("Cliente deve ter pelo menos " + IDADE_MINIMA + " anos para realizar uma compra.");
            }
            clienteExistente.setCpf(clienteAtualizado.getCpf());
            clienteExistente.setNome(clienteAtualizado.getNome());
            clienteExistente.setTelefone(clienteAtualizado.getTelefone());
            clienteExistente.setIdade(clienteAtualizado.getIdade());

            return clienteRepository.save(clienteExistente);
        } else {
            throw new EntityNotFoundException("Cliente não encontrado com o ID: " + id);
        }
    }


    public void excluirCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
