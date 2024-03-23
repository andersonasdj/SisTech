package br.com.techgol.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.techgol.app.dto.DtoLogAcesso;
import br.com.techgol.app.model.LogLogin;
import br.com.techgol.app.repository.LogLoginRepository;

@Component
public class LogLoginService {
	
	@Autowired
	LogLoginRepository repository;

	public void salvaLog(LogLogin login) {
		repository.save(login);
	}
	
	public List<DtoLogAcesso> listarLogs(){
		return repository.findAll().stream().map(DtoLogAcesso::new).toList();
	}
	
}
