package br.com.techgol.app.restcontroller;

import java.time.LocalDateTime;
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

import br.com.techgol.app.dto.DtoCadastroSolicitacao;
import br.com.techgol.app.dto.DtoCadastroSolicitacaoLegada;
import br.com.techgol.app.dto.DtoDadosEdicaoRapida;
import br.com.techgol.app.dto.DtoDadosEdicaoRapidaMaisFuncionarios;
import br.com.techgol.app.dto.DtoDadosParaSolicitacao;
import br.com.techgol.app.dto.DtoDadosRestauracao;
import br.com.techgol.app.dto.DtoDashboardCliente;
import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
import br.com.techgol.app.dto.DtoSolicitacaoFinalizada;
import br.com.techgol.app.dto.DtoSolicitacaoProjecaoCompleta;
import br.com.techgol.app.dto.DtoSolicitacaoRelatorios;
import br.com.techgol.app.dto.dashboard.DtoDashboard;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.DtoUltimaAtualizada;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.repository.FuncionarioRepository;
import br.com.techgol.app.services.SolicitacaoService;

@RestController
@RequestMapping("solicitacao")
public class SolicitacaoRestController {
	
	@Autowired
	private ClienteRepository repositoryCliente;
	
	@Autowired
	private FuncionarioRepository repositoryFuncionario;
	
	@Autowired
	private SolicitacaoService solicitacaoService;
	
	
	@GetMapping //RETORNA TODAS A ENTIDADES DE SOLICITAÇÂO -> LOGO SERÀ DESCONTINUADO
	private List<Solicitacao> listar(){
		return solicitacaoService.buscarTodos();
	}
	
	@GetMapping("short") //RETORNA DTO COM PROJEÇÃO DOS DADOS NECESSÀRIO COM NATIVE QUERY
	public Page<SolicitacaoProjecao> listaResumidaNaoFinalizados(@PageableDefault(size = 100, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoes(page,Status.FINALIZADO.toString(), false);
	}
	
	@GetMapping("/getData") //RETORNA LISTAGEM DE CLIENTES E FUNCIONARIOS ATIVOS PARA LISTAGEM DO SELECTBOX
	private DtoDadosParaSolicitacao coletaDadosParaSolicitacao() {
		
		return new DtoDadosParaSolicitacao(repositoryCliente.listarNomesClienteAtivos(),
				repositoryCliente.listarIdClienteAtivos(), 
				repositoryFuncionario.listarNomesFuncionarios(),
				repositoryFuncionario.listarIdFuncionarios()
				);
	}
	
	@GetMapping("/busca/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO PARA EDIÇÃO RÁPIDA
	public DtoDadosEdicaoRapidaMaisFuncionarios buscaPorId(@PathVariable Long id) {
		List<String> funcionarios = repositoryFuncionario.listarNomesFuncionarios();
		return new DtoDadosEdicaoRapidaMaisFuncionarios(solicitacaoService.buscarPorId(id), funcionarios);
	}
	

	@GetMapping("/finalizada/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO FINALIZADA PARA EDIÇÃO
	public DtoSolicitacaoProjecaoCompleta buscaFinalizadaPorId(@PathVariable Long id) {
		return solicitacaoService.buscarFinalizada(id);
	}
	
	
	@GetMapping("/excluido") //RETORNA DTO COM PROJEÇÃO DAS SOLICITAÇÕES EXCLUIDAS-LIXEIRA
	public Page<SolicitacaoProjecao> excluidas(@PageableDefault(size = 200, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoes(page,Status.FINALIZADO.toString(), true);
	}
	
	
	@GetMapping("/finalizado") //RETORNA DTO COM PROJEÇÃO DE TODAS AS SOLICITACOES EXCLUÍDAS-LIXEIRA
	public Page<SolicitacaoProjecao> finalizados(@PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesFinalizadas(page,Status.FINALIZADO.toString(), false);
	}
	
	@GetMapping("/finalizado/cliente/{id}") //RETORNA UMA DTO COM PROJEÇÃO DE TODAS AS SOLICITAÇÕES FINALIZADAS POR ID DE CLIENTE
	public Page<SolicitacaoProjecao> finalizadasPorCliente(@PathVariable Long id, @PageableDefault(size = 200, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesFinalizadasPorCliente(page, id);
		
	}
	
	@GetMapping("/naofinalizado/cliente/{id}") //RETORNA UMA DTO COM PROJEÇÃO DE TODAS AS SOLICITAÇÕES FINALIZADAS POR ID DE CLIENTE
	public Page<SolicitacaoProjecao> naoFinalizadasPorCliente(@PathVariable Long id, @PageableDefault(size = 200, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesNaoFinalizadasPorCliente(page, id);
		
	}
	
	
	@GetMapping("/dashboard") //RETORNA UMA DTO COM TODOS OS DADOS PARA O DASHBOARD GERAL
	public DtoDashboard dashboard() {
		return solicitacaoService.geraDashboard();
	}
	
	
	@GetMapping("/dashboard/cliente/{id}") //RETORNA UMA DTO COM TODOS OS DADOS PARA O DASHBOARD POR CLIENTE
	public DtoDashboardCliente dashboardCliente(@PathVariable Long id) {
		
		return solicitacaoService.geraDashboardCliente(id);
		
	}
	
	@GetMapping("/relatorio") //RETORNA UMA DTO COM TODOS OS DADOS PARA A VIEWER DE RELATORIOS
	public DtoSolicitacaoRelatorios relatorios() {
		
		return solicitacaoService.geraRelatorios();
		
	}
	
	@GetMapping("/ultima/atualizada") //RETORNA ID E DATA DA ULTIMA SOLICITACAO ATUALIZADA
	public DtoUltimaAtualizada ultimaSolicitacaoAtualizada() {
		return solicitacaoService.ultimaAtualizada();
	}
	
	@PutMapping("/edicaoRapida") //ATUALIZA MODAL DE EDIÇÃO RÁPIDA
	private DtoSolicitacaoComFuncionario edicaoRapida(@RequestBody DtoDadosEdicaoRapida dados) {
		return new DtoSolicitacaoComFuncionario(solicitacaoService.edicaoRapida(dados));
				
	}
	
	@PutMapping("/finalizada/atualizar") //ATUALIZA SOLICITACAO FINALIZADA
	private void edicaoFinalizada(@RequestBody DtoSolicitacaoFinalizada dados) {
		solicitacaoService.edicaoFinalizada(dados);
				
	}
	
	@PutMapping("/restaurar") //RESTAURA UM ITEM ENVIADO PARA LIXEIRA
	private DtoDadosRestauracao restaurar(@RequestBody DtoDadosRestauracao dado) {
		return	solicitacaoService.restaurar(dado.id());
				
	}
	
	@PostMapping("/salvaLista") //RECEBE UMA LISTA DE SOLICITAÇÕES E SALVA NO BANCO
	public void cadastrar(@RequestBody List<DtoCadastroSolicitacaoLegada> dados) {
		dados.forEach(s -> solicitacaoService.salvarNovaSolicitacao(new Solicitacao(s)));
	}
	
	@PostMapping //SALVA UMA NOVA SOLICITAÇÃO NO BANCO
	public DtoSolicitacaoComFuncionario cadastrarNova(@RequestBody DtoCadastroSolicitacao dados ) {
		
		Cliente cliente = repositoryCliente.getReferenceById(dados.nomeCliente());
		Solicitacao solicitacao = new Solicitacao(dados, cliente);
		
		if(dados.nomeFuncionario() != null) {
			Funcionario funcionario = repositoryFuncionario.getReferenceById(dados.nomeFuncionario());
			solicitacao.setFuncionario(funcionario);
		}
		
		if(!dados.dataAgendado().isBlank() || !dados.dataAgendado().isEmpty()) {
			solicitacao.setDataAgendado(LocalDateTime.parse(dados.dataAgendado()+"T"+dados.horaAgendado()));
		}
		return solicitacaoService.salvarNovaSolicitacao(solicitacao);
	}

	@DeleteMapping("/excluir/{id}") //EXCLUSÃO LÓGICA DE UMA SOLICITAÇÃO-ENVIA PARA LIXEIRA
	public String excluir(@PathVariable Long id) {
		return solicitacaoService.exclusaoLogigaSolicitacao(id);
	}
	
}
