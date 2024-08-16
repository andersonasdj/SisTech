package br.com.techgol.app.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.techgol.app.dto.DtoLogAcesso;
import br.com.techgol.app.dto.DtoLogin;
import br.com.techgol.app.model.LogLogin;
import br.com.techgol.app.repository.LogLoginRepository;

@Component
public class LogLoginService {
	
	@Autowired
	LogLoginRepository repository;
	
	@Autowired
	FuncionarioService funcionarioService;

	public void salvaLog(LogLogin login) {
		repository.save(login);
	}
	
	public List<DtoLogAcesso> listarLogs(){
		return repository.lstarTodos().stream().map(DtoLogAcesso::new).toList();
	}

	public DtoLogin buscarPrimeiroLogin(Long id, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			return repository.buscarPrimeiroLogin(funcionarioService.buscaNomeFuncionarioPorId(id), inicio, fim);
		}else {
			inicio = LocalDateTime.now().withNano(0);
			fim = LocalDateTime.now().withNano(0);
			return repository.buscarPrimeiroLogin(funcionarioService.buscaNomeFuncionarioPorId(id), inicio, fim);
		}
		
	}
	
}
