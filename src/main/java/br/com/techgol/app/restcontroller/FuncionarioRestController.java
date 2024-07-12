package br.com.techgol.app.restcontroller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.dto.DtoFuncionarioEdit;
import br.com.techgol.app.dto.DtoFuncionarioHome;
import br.com.techgol.app.dto.DtoFuncionarioRefeicao;
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.dto.DtoSenha;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.orm.DtoFuncionarioEditSimplificado;
import br.com.techgol.app.services.AvisosService;
import br.com.techgol.app.services.FuncionarioService;
import br.com.techgol.app.services.SolicitacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("funcionarios")
public class FuncionarioRestController {
	
	@Autowired
	FuncionarioService service;
	
	@Autowired
	SolicitacaoService solicitacaoService;
	
	@Autowired
	AvisosService avisoService;
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public void cadastrar(@RequestBody @Valid DtoCadastroFuncionario dados ) {
		service.salvar(dados);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping //RETORNA UMA DTO COM A LISTA DE TODOS OS FUNCIONARIOS
	public ResponseEntity<List<DtoListarFuncionarios>> listar(){
		return ResponseEntity.ok().body(service.listar());
	}
	
//	@PreAuthorize("hasRole('ROLE_USER')")
//	@GetMapping("/nomes")
//	public List<String> listaClientesNome(){
//		return service.listarNomesCliente();
//	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/{id}") //RESTORNA UMA DTO DE UM FUNCIONARIO POR ID
	public ResponseEntity<DtoFuncionarioEdit> editar(@PathVariable Long id ) {
		return ResponseEntity.ok().body(service.editar(id));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/perfil") //RETORNA DADOS DO FUNCIONARIO PARA PROPRIA EDICAO (PERFIL)
	public ResponseEntity<DtoFuncionarioEditSimplificado> perfil(){
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		return ResponseEntity.ok().body(service.buscaDadosFuncionario(funcionario.getId()));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/home") //RETORNA UMA DTO COM OS DADOS PARA A HOME PAGE
	public ResponseEntity<DtoFuncionarioHome> funcionarioHome() {
	
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		Date dataHoje = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		DateFormat dateFormatNumber = new SimpleDateFormat("HH");
		
		int hora = Integer.valueOf( dateFormatNumber.format(dataHoje)); 
		String saudacao;
		
		if(hora >= 0 && hora < 12) {
			saudacao = "Bom dia, ";
		} else if (hora >= 12 && hora < 18) {
			saudacao = "Boa tarde, ";
		} else {
			saudacao = "Boa noite, ";
		}
		long qtdAvisos = avisoService.quantidadeAvisos();
		
		
		Boolean trocaSenha = service.exigeTrocaDeSenha(funcionario.getId());
		boolean refeicao = service.statusRefeicao(funcionario.getId());
		
		return ResponseEntity.ok().body(
				new DtoFuncionarioHome(
						saudacao, 
						funcionario.getNomeFuncionario(), 
						dateFormat.format(dataHoje).toString(),
						funcionario.getDataUltimoLogin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
						solicitacaoService.buscaAgendamentosAtrasadosQtd(),
						solicitacaoService.buscaAgendamentosHojeQtd(),
						qtdAvisos,
						(trocaSenha) != null ? trocaSenha : false,
						service.buscaSolicitacoes(funcionario),
						service.buscaSolicitacoesGerais(),
						refeicao
				));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping //ATUALIZA UM FUNCIONARIO
	public ResponseEntity<DtoListarFuncionarios> atualizar(@RequestBody DtoFuncionarioEdit dados) {
		return ResponseEntity.ok().body(service.atualizarFuncionario(dados));
	}
	
	@PutMapping("refeicao") //ATUALIZA UM REFEICAO
	public void atualizarRefeicao(@RequestBody DtoFuncionarioRefeicao dados) {
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		service.alteraStatusRefeicao(funcionario.getId(), dados.refeicao());
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/senha") //ATUALIZA A SENHA PARA QUALQUER USUARIO
	public boolean atualizar(@RequestBody DtoSenha dados) {
		return service.atualizarSenha(dados);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/senha/pessoal") //ATUALIZA A SENHA SOMENTE DO PROPRIO USUARIO
	public boolean atualizarSenhaPessoal(@RequestBody DtoSenha dados) {
		
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		if(funcionario.getId() == dados.id()) {
			return service.atualizarSenha(dados);
		}else {
			return false;
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public boolean deletar(@PathVariable Long id ) {
		return service.deletar(id);
	}
	
}
