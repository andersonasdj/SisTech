package br.com.techgol.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.model.ConfiguracaoEmail;
import br.com.techgol.app.model.ConfiguracaoPaises;
import br.com.techgol.app.model.enums.Agendamentos;
import br.com.techgol.app.services.ConfiguracaoEmailService;
import br.com.techgol.app.services.ConfiguracaoPaisesService;
import br.com.techgol.app.services.FuncionarioService;

@RestController
@RequestMapping()
public class AuthenticatorController {
	
	@Autowired
	private FuncionarioService service;
	
	@Autowired
	private ConfiguracaoPaisesService paisesService;
	
	@Autowired
	private ConfiguracaoEmailService emailService;

	@PostMapping("/create")
	public String register(@RequestBody DtoCadastroFuncionario dados ) {

		if(paisesService.existeConfig() == 0) {
			paisesService.salvar(new ConfiguracaoPaises("BR",true));
			paisesService.salvar(new ConfiguracaoPaises("US",true));
			paisesService.salvar(new ConfiguracaoPaises("PT",true));
			paisesService.salvar(new ConfiguracaoPaises("CA",true));
			paisesService.salvar(new ConfiguracaoPaises("FR",true));
			paisesService.salvar(new ConfiguracaoPaises("CL",true));
		}
		
		if(emailService.existeEmail() == 0) {
			emailService.cadastra(new ConfiguracaoEmail( " ", false, Agendamentos.AGENDAMENTO));
			emailService.cadastra(new ConfiguracaoEmail( " ", false, Agendamentos.ABERTURA));
		}
		
		if(service.existeFuncionarios() == 0) {
			service.salvar(dados);
			return "Usuário cadastrado com sucesso!";
		} else {
			return "Erro!";
		}
	}
	

}
//	@Autowired
//	private AuthenticationManager authenticationManager;

//	@PostMapping("/login")
//	public ResponseEntity<?> login(@RequestBody AuthenticationDTO data)  {
//		
//		System.out.println("CONTROLLER LOGIN POST");
//		
//		String senhaEncriptada = new BCryptPasswordEncoder().encode(data.password());
//		var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), senhaEncriptada);
//		var auth = this.authenticationManager.authenticate(usernamePassword);
//		return ResponseEntity.ok().build();
//	}
