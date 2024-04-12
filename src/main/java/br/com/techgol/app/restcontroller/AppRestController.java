package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoEmails;
import br.com.techgol.app.dto.DtoLogAcesso;
import br.com.techgol.app.dto.DtoPaises;
import br.com.techgol.app.model.ConfiguracaoEmail;
import br.com.techgol.app.model.ConfiguracaoPaises;
import br.com.techgol.app.services.ConfiguracaoEmailService;
import br.com.techgol.app.services.ConfiguracaoPaisesService;
import br.com.techgol.app.services.LogLoginService;

@RestController
@RequestMapping("sistema")
public class AppRestController {
	
	@Autowired
	private LogLoginService loginService;
	
	@Autowired
	private ConfiguracaoPaisesService paisesService;
	
	@Autowired
	private ConfiguracaoEmailService emailService;
	
	@GetMapping("/logs")
	public Page<DtoLogAcesso> logar(@PageableDefault(size = 20, sort= {"id"}, direction = Direction.DESC)Pageable page) {
		return loginService.listarLogs(page);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/configuracao/paises")
	public ResponseEntity<List<ConfiguracaoPaises>> configuracaoPaises(@RequestBody DtoPaises dados) {
		return ResponseEntity.ok().body(paisesService.checarPaises(dados));
	}
	
	@GetMapping("/configuracao/paises")
	public ResponseEntity<List<ConfiguracaoPaises>> buscaConfiguracaoPaises() {
		return ResponseEntity.ok().body(paisesService.listarPaises());
	}
	
	@GetMapping("/configuracao/email")
	public ResponseEntity<List<ConfiguracaoEmail>> buscaConfiguracaoEmails() {
		return ResponseEntity.ok().body(emailService.listarEmails());
	}
	
	@PutMapping("/configuracao/email")
	public void configuracaoEmails(@RequestBody List<DtoEmails> dados) {
		
		emailService.atualiza(dados);
		System.out.println(dados);
	}
	
}
