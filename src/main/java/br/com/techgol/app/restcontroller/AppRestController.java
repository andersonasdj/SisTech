package br.com.techgol.app.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
	public Page<DtoLogAcesso> logar(@PageableDefault(size = 20, sort= {"id"}, direction = Direction.DESC)Pageable page) {
		
		return loginService.listarLogs(page);
		
	}

}
