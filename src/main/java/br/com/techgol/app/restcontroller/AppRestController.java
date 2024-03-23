package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoLogAcesso;
import br.com.techgol.app.services.LogLoginService;

@RestController
@RequestMapping("sistema")
public class AppRestController {
	
	@Autowired
	private LogLoginService loginService;
	
	@GetMapping("/logs")
	public List<DtoLogAcesso> logar() {
		
		return loginService.listarLogs();
		
	}

}
