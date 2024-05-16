package br.com.techgol.app.restcontroller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import br.com.techgol.app.dto.DtoCadastroSolicitacaoModelo;
import br.com.techgol.app.dto.DtoDadosEdicaoRapida;
import br.com.techgol.app.dto.DtoDadosEdicaoRapidaMaisFuncionarios;
import br.com.techgol.app.dto.DtoDadosParaSolicitacao;
import br.com.techgol.app.dto.DtoDadosRestauracao;
import br.com.techgol.app.dto.DtoDashboardCliente;
import br.com.techgol.app.dto.DtoDataAgendado;
import br.com.techgol.app.dto.DtoModelosParaSolicitacao;
import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
import br.com.techgol.app.dto.DtoSolicitacaoFinalizada;
import br.com.techgol.app.dto.DtoSolicitacaoProjecaoCompleta;
import br.com.techgol.app.dto.DtoSolicitacaoProjecaoCompletaColaboradores;
import br.com.techgol.app.dto.DtoSolicitacaoRelatorios;
import br.com.techgol.app.dto.dashboard.DtoDashboard;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.ModeloSolicitacao;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.DtoUltimaAtualizada;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;
import br.com.techgol.app.repository.ConjuntoModelosRepository;
import br.com.techgol.app.repository.FuncionarioRepository;
import br.com.techgol.app.repository.ModeloSolicitacaoRepository;
import br.com.techgol.app.services.ClienteService;
import br.com.techgol.app.services.ColaboradorService;
import br.com.techgol.app.services.SolicitacaoService;

@RestController
@RequestMapping("solicitacao")
public class SolicitacaoRestController {
	
	@Autowired
	FuncionarioRepository repositoryFuncionario;
	
	@Autowired
	SolicitacaoService solicitacaoService;
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	ColaboradorService colaboradorService;
	
	@Autowired
	ConjuntoModelosRepository conjuntoModelosRepository;
	
	@Autowired
	ModeloSolicitacaoRepository modeloSolicitacaoRepository;
	
	@GetMapping("/relatorio/cliente/{id}/inicio/{inicio}/fim/{fim}")
	public Page<SolicitacaoProjecao> listarRelatorioPorClienteDataInicioFim(@PathVariable Long id, @PathVariable LocalDate inicio, @PathVariable LocalDate fim, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
	
	   return solicitacaoService.listarSolicitacoesPorData(page, id, inicio, fim);
	}
	
	@GetMapping("/relatorio/funcionario/{id}/{periodo}/inicio/{inicio}/fim/{fim}")
	public Page<SolicitacaoProjecao> listarRelatorioPorFuncionarioDataInicioFim(@PathVariable Long id, @PathVariable String periodo, @PathVariable LocalDate inicio, @PathVariable LocalDate fim, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
	
	   return solicitacaoService.listarSolicitacoesPorFuncionarioData(page, id, periodo, inicio, fim);
	}
	
	@GetMapping("/relatorio/{status}/hoje")
	public Page<SolicitacaoProjecao> listarRelatorioAtualizadasHoje(@PathVariable String status, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesRelatorioHoje(page, status);
	}
	
	@GetMapping("/relatorio/periodo/inicio/{inicio}/fim/{fim}")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorPeriodoRelatorio(@PathVariable LocalDate inicio, @PathVariable LocalDate fim, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
	
	   return solicitacaoService.listarSolicitacoesPorPeriodo(page, inicio, fim);
	}
	
	//	@GetMapping("/relatorio/atualizadas/hoje")
//	public Page<SolicitacaoProjecao> listarRelatorioAtualizadasHoje(@PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
//	   return solicitacaoService.listarSolicitacoesAtualizadasHoje(page);
//	}
//	@GetMapping("/relatorio/finalizadas/hoje")
//	public Page<SolicitacaoProjecao> listarRelatorioFinalizadasHoje(@PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
//	   return solicitacaoService.listarSolicitacoesFinalizadasHoje(page);
//	}
//	
//	@GetMapping("/relatorio/abertas/hoje")
//	public Page<SolicitacaoProjecao> listarRelatorioAbertasHoje(@PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
//	   return solicitacaoService.listarSolicitacoesAbertasHoje(page);
//	}
	
	
//	@PostMapping("/relatorio/cliente")
//	  public Page<SolicitacaoProjecao> getFileRelatorio(@RequestBody DtoDadosRelatorioCsv dto, @PageableDefault(size = 100, sort= {"id"}, direction = Direction.DESC) Pageable page) {
//	
//	    return solicitacaoService.listarSolicitacoesPorData(page, dto);
//	  }
	
//	@GetMapping //RETORNA TODAS A ENTIDADES DE SOLICITAÇÂO -> LOGO SERÀ DESCONTINUADO
//	private List<Solicitacao> listar(){
//		return solicitacaoService.buscarTodos();
//	}
	
	
	@GetMapping("/agendadodia") // GERA AGENDAMENTO RECEBENDO UMA DATA NO FORMATO "yyyy-mm-dd"
	private List<SolicitacaoProjecaoEntidadeComAtributos> listarAgendadosDoDia(@RequestBody DtoDataAgendado data){
		System.out.println(data.data());
		LocalDateTime.of(data.data(), LocalTime.of(0, 0));
		return solicitacaoService.buscaAgendamentosDoDia(LocalDateTime.of(data.data(), LocalTime.of(0, 0)));
	}
	
	@GetMapping("/agendadas/atrasados") // GERA AGENDAMENTO RECEBENDO UMA DATA NO FORMATO "yyyy-mm-dd"
	private List<SolicitacaoProjecaoEntidadeComAtributos> listarAgendadosAtrasados(){
		return solicitacaoService.buscaAgendamentosAtrasados();
	}
	
	@GetMapping("short") //RETORNA DTO COM PROJEÇÃO DOS DADOS NECESSÀRIO COM NATIVE QUERY
	public Page<SolicitacaoProjecao> listaResumidaNaoFinalizados(@PageableDefault(size = 100, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoes(page,Status.FINALIZADO.toString(), false);
	}
	
	@GetMapping("short/{status}") //RETORNA DTO COM PROJEÇÃO DOS DADOS NECESSÀRIO COM NATIVE QUERY
	public Page<SolicitacaoProjecao> listaResumidaNaoFinalizadosPorStatus(@PathVariable String status,  @PageableDefault(size = 100, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesPorStatus(page,status, false);
	}
	
	@GetMapping("/getData") //RETORNA LISTAGEM DE CLIENTES E FUNCIONARIOS ATIVOS PARA LISTAGEM DO SELECTBOX
	private DtoDadosParaSolicitacao coletaDadosParaSolicitacao() {
		
		return new DtoDadosParaSolicitacao(clienteService.listarNomesClienteAtivos(),
				clienteService.listarIdClienteAtivos(), 
				repositoryFuncionario.listarNomesFuncionarios(),
				repositoryFuncionario.listarIdFuncionarios()
				);
	}
	
	@GetMapping("/getModelos") //RETORNA LISTAGEM DE CLIENTES E FUNCIONARIOS ATIVOS PARA LISTAGEM DO SELECTBOX
	private DtoModelosParaSolicitacao coletaModelosParaSolicitacao() {
		
		return new DtoModelosParaSolicitacao(
				conjuntoModelosRepository.listarNomesModelos(),
				conjuntoModelosRepository.listarIdModelos()
				);
	}
	
	@GetMapping("/busca/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO PARA EDIÇÃO RÁPIDA
	public DtoDadosEdicaoRapidaMaisFuncionarios buscaPorId(@PathVariable Long id) {
		Solicitacao solicitacao = solicitacaoService.buscarPorId(id);
		List<String> funcionarios = repositoryFuncionario.listarNomesFuncionarios();
		List<String> colaboradores = colaboradorService.listarNomesIdCliente(solicitacao.getCliente().getId());
		return new DtoDadosEdicaoRapidaMaisFuncionarios(solicitacaoService.buscarPorId(id), funcionarios, colaboradores);
	}
	

	@GetMapping("/finalizada/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO FINALIZADA PARA EDIÇÃO
	public DtoSolicitacaoProjecaoCompletaColaboradores buscaFinalizadaPorId(@PathVariable Long id) {
		Solicitacao solicitacao = solicitacaoService.buscarPorId(id);
		List<String> funcionarios = repositoryFuncionario.listarNomesFuncionarios();
		List<String> colaboradores = colaboradorService.listarNomesIdCliente(solicitacao.getCliente().getId());
		return new DtoSolicitacaoProjecaoCompletaColaboradores(solicitacaoService.buscarPorId(id), funcionarios, colaboradores);
	}
	
	@GetMapping("/relatorio/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO FINALIZADA PARA EDIÇÃO
	public DtoSolicitacaoProjecaoCompleta buscaSolcitacaoRelatorioPorId(@PathVariable Long id) {
		return solicitacaoService.buscarSolicitacaoRelatorio(id);
	}
	
	@GetMapping("/relatorio/editar/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO FINALIZADA PARA EDIÇÃO
	public DtoSolicitacaoProjecaoCompletaColaboradores buscaRelatorioPorId(@PathVariable Long id) {
		Solicitacao solicitacao = solicitacaoService.buscarPorId(id);
		List<String> funcionarios = repositoryFuncionario.listarNomesFuncionarios();
		List<String> colaboradores = colaboradorService.listarNomesIdCliente(solicitacao.getCliente().getId());
		return new DtoSolicitacaoProjecaoCompletaColaboradores(solicitacaoService.buscarPorId(id), funcionarios, colaboradores);
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
	public Page<SolicitacaoProjecao> finalizadasPorCliente(@PathVariable Long id, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesFinalizadasPorCliente(page, id);
	}
	
	@GetMapping("/finalizado/funcionario/{id}") //RETORNA UMA DTO COM PROJEÇÃO DE TODAS AS SOLICITAÇÕES FINALIZADAS POR ID DE CLIENTE
	public Page<SolicitacaoProjecao> finalizadasPorFuncionario(@PathVariable Long id, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesFinalizadasPorFuncionario(page, id);
	}
	
	@GetMapping("/naofinalizado/cliente/{id}") //RETORNA UMA DTO COM PROJEÇÃO DE TODAS AS SOLICITAÇÕES FINALIZADAS POR ID DE CLIENTE
	public Page<SolicitacaoProjecao> naoFinalizadasPorCliente(@PathVariable Long id, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesNaoFinalizadasPorCliente(page, id);
	}
	
	@GetMapping("/naofinalizado/funcionario/{id}") //RETORNA UMA DTO COM PROJEÇÃO DE TODAS AS SOLICITAÇÕES FINALIZADAS POR ID DE CLIENTE
	public Page<SolicitacaoProjecao> naoFinalizadasPorFuncionario(@PathVariable Long id, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesNaoFinalizadasPorFuncionario(page, id);
	}
	
	
	@GetMapping("/dashboard") //RETORNA UMA DTO COM TODOS OS DADOS PARA O DASHBOARD GERAL
	public DtoDashboard dashboard() {
		return solicitacaoService.geraDashboard();
	}
	
	
	@GetMapping("/dashboard/cliente/{id}") //RETORNA UMA DTO COM TODOS OS DADOS PARA O DASHBOARD POR CLIENTE
	public DtoDashboardCliente dashboardCliente(@PathVariable Long id) {
		return solicitacaoService.geraDashboardCliente(id);
	}
	
	@GetMapping("/dashboard/funcionario/{id}") //RETORNA UMA DTO COM TODOS OS DADOS PARA O DASHBOARD POR CLIENTE
	public DtoDashboardCliente dashboardFuncionario(@PathVariable Long id) {
		return solicitacaoService.geraDashboardFuncionario(id);
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
	private DtoSolicitacaoComFuncionario edicaoFinalizada(@RequestBody DtoSolicitacaoFinalizada dados) {
		return solicitacaoService.edicaoFinalizada(dados);
				
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
		
		Cliente cliente = clienteService.buscaClientePorNome(dados.nomeCliente());
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
	
	@PostMapping("/modelo") //SALVA UMA NOVA SOLICITAÇÃO NO BANCO
	public String cadastrarNovaModelo(@RequestBody DtoCadastroSolicitacaoModelo dados ) {
		
		Cliente cliente = clienteService.buscaClientePorNome(dados.nomeCliente());
		Funcionario funcionario = repositoryFuncionario.getReferenceById(dados.nomeFuncionario());
		
		List<ModeloSolicitacao> modelos = modeloSolicitacaoRepository.buscarSolocitacoesModelosPorIdConjunto(dados.modelo());
		
		if(modelos.size()>0) {
			
			modelos.forEach( m -> {
				
				Solicitacao solicitacao = new Solicitacao();
				solicitacao.setAfetado(dados.afetado());
				solicitacao.setSolicitante(dados.solicitante());
				solicitacao.setDataAbertura(LocalDateTime.now().withNano(0));
				solicitacao.setDataAtualizacao(LocalDateTime.now().withNano(0));
				solicitacao.setFuncionario(funcionario);
				solicitacao.setCliente(cliente);
				solicitacao.setCategoria(m.getCategoria());
				solicitacao.setClassificacao(m.getClassificacao());
				solicitacao.setDescricao(m.getDescricao());
				solicitacao.setFormaAbertura(m.getFormaAbertura());
				solicitacao.setLocal(m.getLocal());
				solicitacao.setObservacao(m.getObservacao());
				solicitacao.setPrioridade(m.getPrioridade());
				solicitacao.setStatus(m.getStatus());
				solicitacao.setExcluido(false);
				solicitacaoService.salvarNovaSolicitacao(solicitacao);
			});
			
		}else {
			return "Erro ao cadastrar modelos";
		}
		
		return "Cadastrado com sucesso!"; 
	}

	@DeleteMapping("/excluir/{id}") //EXCLUSÃO LÓGICA DE UMA SOLICITAÇÃO-ENVIA PARA LIXEIRA
	public String excluir(@PathVariable Long id) {
		return solicitacaoService.exclusaoLogigaSolicitacao(id);
	}
	
}
