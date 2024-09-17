package com.Urna.Service;

import com.Urna.Entity.Eleitor;
import com.Urna.Entity.StatusEleitor;
import com.Urna.Repository.EleitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EleitorService {

	@Autowired
    private EleitorRepository eleitorRepository;

    public Eleitor save(Eleitor eleitor) {
    	if(eleitor.getCpf() == null || eleitor.getCpf().isBlank() || eleitor.getEmail() == null || eleitor.getEmail().isBlank()) 
    		eleitor.setStatus(StatusEleitor.PENDENTE);
    	else
    		eleitor.setStatus(StatusEleitor.APTO);
    	
        return eleitorRepository.save(eleitor);
    }

    public Eleitor update(Long id, Eleitor eleitorAtualizado) {
        if (eleitorRepository.existsById(id)) {
            eleitorAtualizado.setId(id);
            
            Eleitor eleitor = eleitorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Eleitor não encontrado com ID: " + id));
            
            if(eleitor.getStatus() != StatusEleitor.INATIVO) {
	            if(eleitorAtualizado.getCpf() == null || eleitorAtualizado.getCpf().isBlank() || eleitorAtualizado.getEmail() == null || eleitorAtualizado.getEmail().isBlank()) 
	            	eleitorAtualizado.setStatus(StatusEleitor.PENDENTE);
	        	else
	        		eleitorAtualizado.setStatus(StatusEleitor.APTO);
            }else
            	eleitorAtualizado.setStatus(StatusEleitor.INATIVO);
            return eleitorRepository.save(eleitorAtualizado);
        } else {
            throw new RuntimeException("Eleitor não encontrado com ID: " + id);
        }
    }

    public String inativar(Long id) {
        if (eleitorRepository.existsById(id)) {
            eleitorRepository.inativar(id);
            return "Eleitor inativado com sucesso";
        } else {
            throw new RuntimeException("Eleitor não encontrado com ID: " + id);
        }
    }
    
    public String reativar(Long id) {
    	if(eleitorRepository.existsById(id)){
    		
    		Eleitor eleitor = eleitorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Eleitor não encontrado com ID: " + id));
    		
    		if(eleitor.getCpf() == null || eleitor.getCpf().isBlank() || eleitor.getEmail() == null || eleitor.getEmail().isBlank()) 
    			eleitor.setStatus(StatusEleitor.PENDENTE);
        	else
        		eleitorRepository.reativar(id);
    		
    		return "Eleitor reativado com sucesso";
    	}else
    		throw new RuntimeException("Eleitor não encontrado com ID: " + id);
    }
    
    public List<Eleitor> findAll() {
        return eleitorRepository.findAll();
    }
    
    public List<Eleitor> findAllAptos(){
    	return eleitorRepository.findAllAptos();
    }

    public Optional<Eleitor> findById(Long id) {
        return eleitorRepository.findById(id);
    }
}
