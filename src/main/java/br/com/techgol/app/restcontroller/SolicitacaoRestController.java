package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import br.com.techgol.app.dto.DtoDadosParaSolicitacao;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.repository.FuncionarioRepository;
import br.com.techgol.app.repository.SolicitacaoRepository;

@RestController
@RequestMapping("solicitacao")
public class SolicitacaoRestController {
	
	@Autowired
	private SolicitacaoRepository repository;
	@Autowired
	private ClienteRepository repositoryCliente;
	@Autowired
	private FuncionarioRepository repositoryFuncionario;
	
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
		
		Solicitacao solicitacao = repository.getReferenceById(dados.id());
		
		solicitacao.setDescricao(dados.descricao());
		solicitacao.setResolucao(dados.resolucao());
		solicitacao.setObservacao(dados.observacao());
		solicitacao.setStatus(dados.status());
		return repository.save(solicitacao);
				
	}
	
	@GetMapping("/busca/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO PARA EDIÇÃO RÁPIDA
	public DtoDadosEdicaoRapida buscaPorId(@PathVariable Long id) {
		
		return new DtoDadosEdicaoRapida(repository.getReferenceById(id));
		
	}
	
	
	@PostMapping("/salvaLista") //RECEBE UMA LISTA DE SOLICITAÇÕES E SALVA NO BANCO
	public void cadastrar(@RequestBody List<DTOCadastroSolicitacao> dados) {
		
		dados.forEach(s -> repository.save(new Solicitacao(s)));
		
	}
	
	@PostMapping //SALVA UMA NOVA SOLICITAÇÃO NO BANCO
	public String cadastrarNova(@RequestBody DtoCadastroSolicitacao dados ) {
		
		Cliente cliente = repositoryCliente.getReferenceById(dados.nomeCliente());

		System.out.println(dados.nomeCliente());
		System.out.println(dados.nomeFuncionario());
		
		
		
		if(dados.nomeFuncionario() != null) {
			Funcionario funcionario = repositoryFuncionario.getReferenceById(dados.nomeFuncionario());
			repository.save(new Solicitacao(dados, cliente, funcionario));
		}else {
			repository.save(new Solicitacao(dados, cliente));
		}
		
		return "Solicitação cadastrada com sucesso!";
	}

	
}
