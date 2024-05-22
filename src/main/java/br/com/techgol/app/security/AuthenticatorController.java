package br.com.techgol.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.model.ConfiguracaoEmail;
import br.com.techgol.app.model.ConfiguracaoPaises;
import br.com.techgol.app.model.PesoSolicitacao;
import br.com.techgol.app.model.enums.Agendamentos;
import br.com.techgol.app.repository.PesoSolicitacoes;
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
	
	@Autowired
	private PesoSolicitacoes pesoSolicitacoes;

	@PostMapping("/create")
	public String register(@RequestBody DtoCadastroFuncionario dados ) {

		if(pesoSolicitacoes.existsConfigPesos() == 0) {
			pesoSolicitacoes.save(new PesoSolicitacao("VIP", 0l, "Cliente"));
			pesoSolicitacoes.save(new PesoSolicitacao("REDFLAG", 0l, "Cliente"));
			pesoSolicitacoes.save(new PesoSolicitacao("INCIDENTE", 0l, "Classificação"));
			pesoSolicitacoes.save(new PesoSolicitacao("PROBLEMA", 0l, "Classificação"));
			pesoSolicitacoes.save(new PesoSolicitacao("SOLICITACAO", 0l, "Classificação"));
			pesoSolicitacoes.save(new PesoSolicitacao("ACESSO", 0l, "Classificação"));
			pesoSolicitacoes.save(new PesoSolicitacao("EVENTO", 0l, "Classificação"));
			pesoSolicitacoes.save(new PesoSolicitacao("BACKUP", 0l, "Classificação"));
			pesoSolicitacoes.save(new PesoSolicitacao("BAIXA", 0l, "Prioridade"));
			pesoSolicitacoes.save(new PesoSolicitacao("MEDIA", 0l, "Prioridade"));
			pesoSolicitacoes.save(new PesoSolicitacao("ALTA", 0l, "Prioridade"));
			pesoSolicitacoes.save(new PesoSolicitacao("CRITICA", 0l, "Prioridade"));
			pesoSolicitacoes.save(new PesoSolicitacao("PLANEJADA", 0l, "Prioridade"));
		}
		
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
