package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.orm.RefeicaoProjecao;
import br.com.techgol.app.services.TimeRefeicaoServices;

@RestController
@RequestMapping("api/v1/timerefeicao")
public class TimeRefeicaoRestController {
	
	@Autowired
	TimeRefeicaoServices refeicaoServices;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/listar")
	public List<RefeicaoProjecao> listarUltimos50(){
		return refeicaoServices.listarUltimos50();
		
	}

}
