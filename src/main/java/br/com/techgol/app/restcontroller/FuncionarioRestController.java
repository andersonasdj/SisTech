package br.com.techgol.app.restcontroller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.dto.DtoSenha;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.orm.DtoFuncionarioEditSimplificado;
import br.com.techgol.app.services.FuncionarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("funcionarios")
public class FuncionarioRestController {
	
	@Autowired
	FuncionarioService service;
	
	@PostMapping
	public void cadastrar(@RequestBody @Valid DtoCadastroFuncionario dados ) {
		service.salvar(dados);
	}
	
	@GetMapping
	public List<DtoListarFuncionarios> listar(){
		return service.listar();
	}
	
	@GetMapping("/perfil")
	public DtoFuncionarioEditSimplificado perfil(){
		
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		return service.buscaDadosFuncionario(funcionario.getId());
	}
	
	@GetMapping("/home")
	public DtoFuncionarioHome funcionarioHome() {
	
		//Funcionario funcionario = (Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
		return new DtoFuncionarioHome(
						saudacao, 
						funcionario.getNomeFuncionario(), 
						dateFormat.format(dataHoje).toString(),
						funcionario.getDataUltimoLogin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
						service.buscaSolicitacoes(funcionario),
						service.buscaSolicitacoesGerais()
					);
	}
	
	@GetMapping("/nomes")
	public List<String> listaClientesNome(){
		return service.listarNomesCliente();
	}
	
	
	@GetMapping("/{id}")
	public DtoFuncionarioEdit editar(@PathVariable Long id ) {
		return service.editar(id);
	}
	
	@DeleteMapping("/{id}")
	public boolean deletar(@PathVariable Long id ) {
		return service.deletar(id);
	}
	
	@PutMapping
	public DtoListarFuncionarios atualizar(@RequestBody DtoFuncionarioEdit dados) {
		return service.atualizarFuncionario(dados);
	}
	
	@PutMapping("/senha")
	public boolean atualizar(@RequestBody DtoSenha dados) {
		return service.atualizarSenha(dados);
	}
	
	@PutMapping("/senha/pessoal")
	public boolean atualizarSenhaPessoal(@RequestBody DtoSenha dados) {
		
		Funcionario funcionario = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		if(funcionario.getId() == dados.id()) {
			return service.atualizarSenha(dados);
		}else {
			return false;
		}
		
	}
	
}
