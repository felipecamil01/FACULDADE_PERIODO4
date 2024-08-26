package com.example.LOJA.Service;

import com.example.LOJA.Entity.Cliente;
import com.example.LOJA.Entity.Funcionario;
import com.example.LOJA.repository.FuncionarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private static final int IDADE_MINIMA_FUNCIONARIO = 18;

    public Funcionario save(Funcionario funcionario) {

        if (funcionario.getIdade() < IDADE_MINIMA_FUNCIONARIO) {
            throw new IllegalArgumentException("O funcionário deve ter pelo menos " + IDADE_MINIMA_FUNCIONARIO + " anos para ser registrado.");
        }

        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> buscarTodosFuncionario() {
        return funcionarioRepository.findAll();
    }


    public Optional<Funcionario> buscarFuncionarioPorId (Long id) {
        return funcionarioRepository.findById(id);
    }


    public Funcionario atualizarFuncionario(Long id, Funcionario funcionarioAtualizado) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(id);

        if (funcionarioOptional.isPresent()) {
            Funcionario funcionarioExistente = funcionarioOptional.get();
            if (funcionarioAtualizado.getIdade() < IDADE_MINIMA_FUNCIONARIO) {
                throw new IllegalArgumentException("O funcionário deve ter pelo menos " + IDADE_MINIMA_FUNCIONARIO + " anos para ser registrado.");
            }
            funcionarioExistente.setMatricula(funcionarioAtualizado.getMatricula());
            funcionarioExistente.setNome(funcionarioAtualizado.getNome());
            funcionarioExistente.setIdade(funcionarioAtualizado.getIdade());
            return funcionarioRepository.save(funcionarioExistente);
        } else {
            throw new EntityNotFoundException("Funcionario não encontrado com o ID: " + id);
        }
    }


    public void excluirFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }
}
