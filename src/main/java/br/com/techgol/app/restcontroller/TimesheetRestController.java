package br.com.techgol.app.restcontroller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.services.TimeSheetService;

@RestController
@RequestMapping("api/v1/timesheet")
public class TimesheetRestController {
	
	@Autowired
	private TimeSheetService service;
	
	@GetMapping("/id/{id}/{inicio}/{fim}")
	public Long minutosPorFuncionarioPeriodo(@PathVariable Long id, @PathVariable LocalDate inicio, @PathVariable LocalDate fim) {
		return service.minutosPorFuncionarioPeriodo(id, inicio, fim);
	}

}
