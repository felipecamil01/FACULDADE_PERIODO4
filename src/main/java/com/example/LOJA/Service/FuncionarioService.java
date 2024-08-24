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
    public Funcionario save(Funcionario funcionario) {
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

            funcionarioExistente.setMatricula(funcionarioAtualizado.getMatricula());
            funcionarioExistente.setNome(funcionarioAtualizado.getNome());
            funcionarioExistente.setIdade(funcionarioAtualizado.getIdade());
            return funcionarioRepository.save(funcionarioExistente);
        } else {
            throw new EntityNotFoundException("Funcionario não encontrado com o ID: " + id);
        }
    }


    public void excluirCliente(Long id) {
        funcionarioRepository.deleteById(id);
    }
}
