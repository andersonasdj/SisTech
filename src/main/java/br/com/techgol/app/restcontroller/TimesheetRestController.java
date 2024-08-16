package br.com.techgol.app.restcontroller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.orm.TimelineProjecao;
import br.com.techgol.app.repository.FuncionarioRepository;
import br.com.techgol.app.services.TimeSheetService;

@RestController
@RequestMapping("api/v1/timesheet")
public class TimesheetRestController {
	
	@Autowired
	private TimeSheetService service;
	
	@Autowired
	private FuncionarioRepository repositoryFuncionario;
	
	@GetMapping("/id/{id}/{inicio}/{fim}")
	public Long minutosPorFuncionarioPeriodo(@PathVariable Long id, @PathVariable LocalDate inicio, @PathVariable LocalDate fim) {
		
		Funcionario funcionarioBase = repositoryFuncionario.findBynomeFuncionario(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		if(funcionarioBase.getRole().toString().equals("ADMIN")) {
			return service.minutosPorFuncionarioPeriodo(id, inicio, fim);
			
		}else {
			return 0l;
		}
		
		
	}

	@GetMapping("timeline/id/{id}/{inicio}/{fim}")
	public List<TimelineProjecao> timelinePorFuncionarioPeriodo(@PathVariable Long id, @PathVariable LocalDate inicio, @PathVariable LocalDate fim) {
		return service.timelinePorFuncionarioPeriodo(id, inicio, fim);
	}
	
}
