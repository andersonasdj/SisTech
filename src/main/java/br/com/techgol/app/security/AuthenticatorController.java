package br.com.techgol.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.services.FuncionarioService;

@RestController
@RequestMapping()
public class AuthenticatorController {
	
	@Autowired
	private FuncionarioService service;

	@PostMapping("/create")
	public String register(@RequestBody DtoCadastroFuncionario dados ) {
		
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
