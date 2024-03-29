package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoLogAcesso;
import br.com.techgol.app.dto.DtoPaises;
import br.com.techgol.app.model.ConfiguracaoPaises;
import br.com.techgol.app.services.ConfiguracaoPaisesService;
import br.com.techgol.app.services.LogLoginService;

@RestController
@RequestMapping("sistema")
public class AppRestController {
	
	@Autowired
	private LogLoginService loginService;
	
	@Autowired
	private ConfiguracaoPaisesService paisesService;
	
	@GetMapping("/logs")
	public Page<DtoLogAcesso> logar(@PageableDefault(size = 20, sort= {"id"}, direction = Direction.DESC)Pageable page) {
		return loginService.listarLogs(page);
	}

	
	@PostMapping("/configuracao/paises")
	public ResponseEntity<List<ConfiguracaoPaises>> configuracaoPaises(@RequestBody DtoPaises dados) {
		
		return ResponseEntity.ok().body(paisesService.checarPaises(dados));
		
	}
	
	
	@GetMapping("/configuracao/paises")
	public ResponseEntity<List<ConfiguracaoPaises>> buscaConfiguracaoPaises() {
		return ResponseEntity.ok().body(paisesService.listarPaises());
	}
	
}
