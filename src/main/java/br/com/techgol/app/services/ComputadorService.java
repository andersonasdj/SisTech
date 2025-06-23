package br.com.techgol.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoComputadorResumido;
import br.com.techgol.app.model.Computador;
import br.com.techgol.app.repository.ComputadorRepository;
import jakarta.transaction.Transactional;

@Service
public class ComputadorService {
	
	@Autowired ComputadorRepository repository;
	
	
	@Transactional
	public String atualizarComputador(DtoComputadorResumido dto) {
		
		if(repository.existsById(dto.id())) {
			Computador computador = repository.getReferenceById(dto.id());
			computador.setComment(dto.comment());
			computador.setStorageMonitor(dto.storageMonitor());
			computador.setStatusMonitor(dto.statusMonitor());
			return "Computador atualizado com sucesso!";
		}else {
			return "Não foi possível atualizar!";
		}
		
	}

}
