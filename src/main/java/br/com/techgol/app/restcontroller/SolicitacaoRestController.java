package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DTOCadastroSolicitacao;
import br.com.techgol.app.dto.DtoCadastroSolicitacao;
import br.com.techgol.app.dto.DtoDadosEdicaoRapida;
import br.com.techgol.app.dto.DtoDadosEdicaoRapidaMaisFuncionarios;
import br.com.techgol.app.dto.DtoDadosParaSolicitacao;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.repository.FuncionarioRepository;
import br.com.techgol.app.repository.SolicitacaoRepository;
import br.com.techgol.app.services.SolicitacaoService;

@RestController
@RequestMapping("solicitacao")
public class SolicitacaoRestController {
	
	@Autowired
	private SolicitacaoRepository repository;
	@Autowired
	private ClienteRepository repositoryCliente;
	@Autowired
	private FuncionarioRepository repositoryFuncionario;
	@Autowired
	private SolicitacaoService solicitacaoService;
	
	@GetMapping //RETORNA TODAS A ENTIDADES DE SOLICITAÇÂO -> LOGO SERÀ DESCONTINUADO
	private List<Solicitacao> listar(){
		return repository.findAll();
	}
	
	
	@GetMapping("short") //RETORNA DTO COM PROJEÇÃO DOS DADOS NECESSÀRIO COM NATIVE QUERY
	public Page<SolicitacaoProjecao> listaResumida(@PageableDefault(size = 200, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		
		return repository.listarSolicitacoes(page);
		
	}
	
	@GetMapping("/getData") //RETORNA LISTAGEM DE CLIENTES E FUNCIONARIOS ATIVOS
	private DtoDadosParaSolicitacao coletaDadosParaSolicitacao() {
		
		return new DtoDadosParaSolicitacao(repositoryCliente.listarNomesCliente(),
				repositoryCliente.listarIdCliente(), 
				repositoryFuncionario.listarNomesFuncionarios(),
				repositoryFuncionario.listarIdFuncionarios()
				);
	}
	
	@PutMapping("/edicaoRapida") //ATUALIZA MODAL DE EDIÇÃO RÁPIDA
	private Solicitacao edicaoRapida(@RequestBody DtoDadosEdicaoRapida dados) {
		
		return solicitacaoService.edicaoRapida(dados);
				
	}
	
	@GetMapping("/busca/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO PARA EDIÇÃO RÁPIDA
	public DtoDadosEdicaoRapidaMaisFuncionarios buscaPorId(@PathVariable Long id) {
		List<String> funcionarios = repositoryFuncionario.listarNomesFuncionarios();
		return new DtoDadosEdicaoRapidaMaisFuncionarios(repository.getReferenceById(id), funcionarios);
	}
	
	
	@PostMapping("/salvaLista") //RECEBE UMA LISTA DE SOLICITAÇÕES E SALVA NO BANCO
	public void cadastrar(@RequestBody List<DTOCadastroSolicitacao> dados) {
		
		dados.forEach(s -> repository.save(new Solicitacao(s)));
		
	}
	
	@PostMapping //SALVA UMA NOVA SOLICITAÇÃO NO BANCO
	public String cadastrarNova(@RequestBody DtoCadastroSolicitacao dados ) {
		
		Cliente cliente = repositoryCliente.getReferenceById(dados.nomeCliente());
		
		if(dados.nomeFuncionario() != null) {
			Funcionario funcionario = repositoryFuncionario.getReferenceById(dados.nomeFuncionario());
			repository.save(new Solicitacao(dados, cliente, funcionario));
		}else {
			repository.save(new Solicitacao(dados, cliente));
		}
		
		return "Solicitação cadastrada com sucesso!";
	}


	@DeleteMapping("/excluir/{id}")
	public String excluir(@PathVariable Long id) {
		
		return solicitacaoService.exclusaoLogigaSolicitacao(id);
		
	}
	
}
