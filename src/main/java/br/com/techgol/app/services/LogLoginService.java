package br.com.techgol.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public Page<DtoLogAcesso> listarLogs(Pageable page){
		return repository.lstarTodos(page).map(DtoLogAcesso::new);
	}
	
}
