package br.com.techgol.app.restcontroller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoCadastroSolicitacao;
import br.com.techgol.app.dto.DtoCadastroSolicitacaoModelo;
import br.com.techgol.app.dto.DtoDadosEdicaoRapida;
import br.com.techgol.app.dto.DtoDadosEdicaoRapidaMaisFuncionarios;
import br.com.techgol.app.dto.DtoDadosParaSolicitacao;
import br.com.techgol.app.dto.DtoDadosRestauracao;
import br.com.techgol.app.dto.DtoDashboardCliente;
import br.com.techgol.app.dto.DtoDashboardFuncionarios;
import br.com.techgol.app.dto.DtoDataAgendado;
import br.com.techgol.app.dto.DtoModelosParaSolicitacao;
import br.com.techgol.app.dto.DtoRelatorioFuncionario;
import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
import br.com.techgol.app.dto.DtoSolicitacaoFinalizada;
import br.com.techgol.app.dto.DtoSolicitacaoProjecaoCompleta;
import br.com.techgol.app.dto.DtoSolicitacaoProjecaoCompletaColaboradores;
import br.com.techgol.app.dto.DtoSolicitacaoRelatorios;
import br.com.techgol.app.dto.dashboard.DtoDashboard;
import br.com.techgol.app.dto.dashboard.DtoDashboardGerencia;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.ModeloSolicitacao;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.DtoUltimaAtualizada;
import br.com.techgol.app.orm.ProjecaoDadosImpressao;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;
import br.com.techgol.app.repository.ConjuntoModelosRepository;
import br.com.techgol.app.repository.FuncionarioRepository;
import br.com.techgol.app.repository.ModeloSolicitacaoRepository;
import br.com.techgol.app.services.ClienteService;
import br.com.techgol.app.services.ColaboradorService;
import br.com.techgol.app.services.ConjuntoModeloSolicitacaoService;
import br.com.techgol.app.services.FuncionarioService;
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
	FuncionarioService funcionarioService;
	
	@Autowired
	ColaboradorService colaboradorService;
	
	@Autowired
	ConjuntoModeloSolicitacaoService conjuntoModeloSolicitacaoService;
	
	@Autowired
	ConjuntoModelosRepository conjuntoModelosRepository;
	
	@Autowired
	ModeloSolicitacaoRepository modeloSolicitacaoRepository;
	
	
	@GetMapping("/relatorio/periodo/{periodo}/inicio/{inicio}/fim/{fim}/{abertura}/{categoria}/{classificacao}/{local}/{prioridade}/{status}")
	public Page<SolicitacaoProjecao> listarRelatorioPorPeriodo(@PathVariable String periodo, @PathVariable LocalDate inicio, @PathVariable LocalDate fim, 
			@PathVariable String abertura, @PathVariable String categoria, @PathVariable String classificacao, @PathVariable String local, @PathVariable String prioridade, 
			@PathVariable String status, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
	   return solicitacaoService.listarSolicitacoesPorPeriodo(page, periodo, inicio, fim, abertura, categoria, classificacao, local, prioridade, status);
	}
	
	@GetMapping("gerencia") //RETORNA DTO COM PROJEÇÃO DOS DADOS NECESSÀRIO COM NATIVE QUERY
	public Page<DtoDashboardGerencia> gerarDashboardGerencia() {
		return solicitacaoService.gerarDashboardGerencia();
	}
	
	@GetMapping("palavra/{conteudo}") //RETORNA DTO COM PROJEÇÃO DOS DADOS NECESSÀRIO COM NATIVE QUERY
	public Page<SolicitacaoProjecao> buscarSolicitacoesPorPalavras(@PathVariable String conteudo,  @PageableDefault(size = 100, sort= {"peso"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesPorPalavra(page,conteudo);
	}
	
	@GetMapping("/relatorio/cliente/{id}/{periodo}/inicio/{inicio}/fim/{fim}")
	public Page<SolicitacaoProjecao> listarRelatorioPorClienteDataInicioFim(@PathVariable Long id, @PathVariable String periodo, @PathVariable LocalDate inicio, @PathVariable LocalDate fim, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
	   return solicitacaoService.listarSolicitacoesPorData(page, id, periodo, inicio, fim);
	}
	
	@GetMapping("/relatorio/cliente/{id}/{periodo}/inicio/{inicio}/fim/{fim}/{abertura}/{categoria}/{classificacao}/{local}/{prioridade}/{nomeFuncionario}")
	public Page<SolicitacaoProjecao> listarRelatorioPorFiltro(@PathVariable Long id, @PathVariable String periodo, @PathVariable LocalDate inicio, @PathVariable LocalDate fim, 
			@PathVariable String abertura, @PathVariable String categoria, @PathVariable String classificacao, @PathVariable String local, @PathVariable String prioridade, 
			@PathVariable String nomeFuncionario, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
	   return solicitacaoService.listarSolicitacoesPorFiltro(page, id, periodo, inicio, fim, abertura, categoria, classificacao, local, prioridade, nomeFuncionario);
	}
	
	@GetMapping("/relatorio/funcionario/{id}/{periodo}/inicio/{inicio}/fim/{fim}")
	public Page<SolicitacaoProjecao> listarRelatorioPorFuncionarioDataInicioFim(@PathVariable Long id, @PathVariable String periodo , @PathVariable LocalDate inicio, @PathVariable LocalDate fim, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		Funcionario funcionarioBase = repositoryFuncionario.findBynomeFuncionario(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		if(funcionarioBase.getRole().toString().equals("ADMIN")) {
			return solicitacaoService.listarSolicitacoesPorFuncionarioData(page, id, periodo, inicio, fim);
		}else {
			return solicitacaoService.listarSolicitacoesPorFuncionarioData(page, funcionarioBase.getId(), periodo, inicio, fim);
		}
	}
	
	@GetMapping("/relatorio/grafico/funcionario/{id}/{periodo}/inicio/{inicio}/fim/{fim}")
	public DtoDashboardFuncionarios listarRelatorioGraficoPorFuncionarioDataInicioFim(@PathVariable Long id, @PathVariable String periodo , @PathVariable LocalDate inicio, @PathVariable LocalDate fim, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		Funcionario funcionarioBase = repositoryFuncionario.findBynomeFuncionario(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		if(funcionarioBase.getRole().toString().equals("ADMIN")) {
			return solicitacaoService.geraDashboardFuncionarioPorPeriodo(id, periodo, inicio, fim);
		}else {
			return solicitacaoService.geraDashboardFuncionarioPorPeriodo(funcionarioBase.getId(), periodo, inicio, fim);
		}
	}
	
	@GetMapping("/relatorio/funcionario/numeros/{id}/inicio/{inicio}/fim/{fim}")
	public DtoRelatorioFuncionario listarNumerosRelatorioPorFuncionarioDataInicioFim(@PathVariable Long id, @PathVariable LocalDate inicio, @PathVariable LocalDate fim) {

		Funcionario funcionarioBase = repositoryFuncionario.findBynomeFuncionario(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		if(funcionarioBase.getRole().toString().equals("ADMIN")) {
			return solicitacaoService.listarRelatoriosPorFuncionarioData(id, inicio, fim);
		}else {
			return solicitacaoService.listarRelatoriosPorFuncionarioData(funcionarioBase.getId(), inicio, fim);
		}
	}
	
	@GetMapping("/relatorio/grafico/cliente/{id}/{periodo}/inicio/{inicio}/fim/{fim}")
	public DtoDashboardFuncionarios listarRelatorioGraficoPorClienteDataInicioFim(@PathVariable Long id, @PathVariable String periodo , @PathVariable LocalDate inicio, @PathVariable LocalDate fim, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.geraDashboardClientePorPeriodo(id, periodo, inicio, fim);
	}
	
	@GetMapping("/relatorio/cliente/numeros/{id}/inicio/{inicio}/fim/{fim}")
	public DtoRelatorioFuncionario listarNumerosRelatorioPorClienteDataInicioFim(@PathVariable Long id, @PathVariable LocalDate inicio, @PathVariable LocalDate fim) {
		return solicitacaoService.listarRelatoriosPorClienteData(id, inicio, fim);
	}
	
	@GetMapping("/relatorio/{status}/hoje")
	public Page<SolicitacaoProjecao> listarRelatorioAtualizadasHoje(@PathVariable String status, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesRelatorioHoje(page, status);
	}
	
	@GetMapping("/relatorio/periodo/inicio/{inicio}/fim/{fim}")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorPeriodoRelatorio(@PathVariable LocalDate inicio, @PathVariable LocalDate fim, @PageableDefault(size = 50, sort= {"id"}, direction = Direction.DESC) Pageable page) {
	
	   return solicitacaoService.listarSolicitacoesPorPeriodo(page, inicio, fim);
	}
	
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
	public Page<SolicitacaoProjecao> listaResumidaNaoFinalizados(@PageableDefault(size = 100, sort= {"peso"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoes(page,Status.FINALIZADO.toString(), false);
	}
	
	@GetMapping("short/{status}") //RETORNA DTO COM PROJEÇÃO DOS DADOS NECESSÀRIO COM NATIVE QUERY
	public Page<SolicitacaoProjecao> listaResumidaNaoFinalizadosPorStatus(@PathVariable String status,  @PageableDefault(size = 100, sort= {"peso"}, direction = Direction.DESC) Pageable page) {
		return solicitacaoService.listarSolicitacoesPorStatus(page,status, false);
	}
	
	@GetMapping("/getData") //RETORNA LISTAGEM DE CLIENTES E FUNCIONARIOS ATIVOS PARA LISTAGEM DO SELECTBOX ### CACHE ###
	private DtoDadosParaSolicitacao coletaDadosParaSolicitacao() {
		Funcionario funcionarioBase = repositoryFuncionario.findBynomeFuncionario(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		return new DtoDadosParaSolicitacao(clienteService.listarNomesClienteAtivos(),
				clienteService.listarIdClienteAtivos(), 
				funcionarioService.listarNomesFuncionariosAtivos(),
				funcionarioService.listarIdFuncionariosAtivos(),
				repositoryFuncionario.statusRefeicao(funcionarioBase.getId())
				);
	}
	
	@GetMapping("/getDataUser") //RETORNA LISTAGEM DE CLIENTES E FUNCIONARIOS ATIVOS PARA LISTAGEM DO SELECTBOX ### CACHE ###
	private DtoDadosParaSolicitacao listagemUsuariosAtivosCondicional() {
		Funcionario funcionarioBase = repositoryFuncionario.findBynomeFuncionario(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		if(funcionarioBase.getRole().toString().equals("ADMIN")) {
			return new DtoDadosParaSolicitacao(clienteService.listarNomesClienteAtivos(),
					clienteService.listarIdClienteAtivos(), 
					funcionarioService.listarNomesFuncionariosAtivos(),
					funcionarioService.listarIdFuncionariosAtivos(),
					repositoryFuncionario.statusRefeicao(funcionarioBase.getId())
					);
		
		}else {
			List<String> nomesFuncionarios = new ArrayList<>();
			List<String> idFuncionarios = new ArrayList<>();
			nomesFuncionarios.add(funcionarioBase.getNomeFuncionario());
			idFuncionarios.add(funcionarioBase.getId().toString());
		
		return new DtoDadosParaSolicitacao(clienteService.listarNomesClienteAtivos(),
				clienteService.listarIdClienteAtivos(), 
				nomesFuncionarios,
				idFuncionarios,
				repositoryFuncionario.statusRefeicao(funcionarioBase.getId())
				);
		}
	}
	
	@GetMapping("/getModelos") //RETORNA LISTAGEM DE CLIENTES E FUNCIONARIOS ATIVOS PARA LISTAGEM DO SELECTBOX ### CACHE ###
	private DtoModelosParaSolicitacao coletaModelosParaSolicitacao() {
		return new DtoModelosParaSolicitacao(
				conjuntoModeloSolicitacaoService.listarNomesModelos(),
				conjuntoModeloSolicitacaoService.listarIdModelos()
				);
	}
	
	@GetMapping("/busca/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO PARA EDIÇÃO RÁPIDA
	public ResponseEntity<DtoDadosEdicaoRapidaMaisFuncionarios> buscaPorId(@PathVariable Long id) {
		Funcionario funcionarioBase = repositoryFuncionario.findBynomeFuncionario(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		Solicitacao solicitacao = solicitacaoService.buscarPorId(id);
		
		if(repositoryFuncionario.statusRefeicao(funcionarioBase.getId())) {
			return ResponseEntity.ok().body(new DtoDadosEdicaoRapidaMaisFuncionarios(solicitacaoService.buscarPorId(id), null, null));
		}else {
			List<String> funcionarios = repositoryFuncionario.listarNomesFuncionarios();
			List<String> colaboradores = colaboradorService.listarNomesIdCliente(solicitacao.getCliente().getId());
			return ResponseEntity.ok().body( new DtoDadosEdicaoRapidaMaisFuncionarios(solicitacaoService.buscarPorId(id), funcionarios, colaboradores));
		}
	}
	
	@GetMapping("/busca/card/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO PARA EDIÇÃO RÁPIDA
	public ResponseEntity<DtoDadosEdicaoRapidaMaisFuncionarios> buscaCardPorId(@PathVariable Long id) {
			return ResponseEntity.ok().body( new DtoDadosEdicaoRapidaMaisFuncionarios(solicitacaoService.buscarPorId(id), null, null));
	}
	
	@GetMapping("/impressao/{id}") //RETORNA UMA DTO DE UMA SOLICITAÇÃO PARA EDIÇÃO RÁPIDA
	public ProjecaoDadosImpressao impressaoPorId(@PathVariable Long id) {
		return solicitacaoService.impressao(id);
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
	public List<SolicitacaoProjecao> finalizados() {
		return solicitacaoService.listarSolicitacoesFinalizadas(Status.FINALIZADO.toString(), false);
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
	
	
//	@PostMapping //SALVA UMA NOVA SOLICITAÇÃO NO BANCO
//	public DtoSolicitacaoComFuncionario cadastrarNova(@RequestBody DtoCadastroSolicitacao dados ) {
//		
//		Cliente cliente = clienteService.buscaClientePorNome(dados.nomeCliente());
//		Solicitacao solicitacao = new Solicitacao(dados, cliente);
//		
//		if(dados.nomeFuncionario() != null) {
//			Funcionario funcionario = repositoryFuncionario.getReferenceById(dados.nomeFuncionario());
//			solicitacao.setFuncionario(funcionario);
//		}
//		
//		if(!dados.dataAgendado().isBlank() || !dados.dataAgendado().isEmpty()) {
//			solicitacao.setDataAgendado(LocalDateTime.parse(dados.dataAgendado()+"T"+dados.horaAgendado()));
//		}
//		return solicitacaoService.salvarNovaSolicitacao(solicitacao); 
//	}
	
	
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
		
		if(dados.status().equals(Status.FINALIZADO) && !dados.dataAndamento().isBlank() && !dados.dataFinalizado().isBlank()) {
			solicitacao.setDataAbertura(LocalDateTime.parse(dados.dataAndamento()+"T"+dados.horaAndamento()));
			solicitacao.setDataAndamento(LocalDateTime.parse(dados.dataAndamento()+"T"+dados.horaAndamento()));
			solicitacao.setDataFinalizado(LocalDateTime.parse(dados.dataFinalizado()+"T"+dados.horaFinalizado()));
			solicitacao.setStatus(Status.FINALIZADO);
			solicitacao.setDuracao(Duration.between(solicitacao.getDataAndamento(), solicitacao.getDataFinalizado()).toMinutes());
			solicitacao.setResolucao(dados.resolucao());
			return solicitacaoService.salvarNovaSolicitacao(solicitacao); 
		}else {
			return solicitacaoService.salvarNovaSolicitacao(solicitacao); 
		}
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
				solicitacao.setDuracao(0l);
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
	
	@PostMapping("/notificar/{id}") //EXCLUSÃO LÓGICA DE UMA SOLICITAÇÃO-ENVIA PARA LIXEIRA
	public String notificarCliente(@PathVariable Long id) {
		return solicitacaoService.notificarCliente(id);
	}
	
	@PutMapping("/cancelar/{id}") //EXCLUSÃO LÓGICA DE UMA SOLICITAÇÃO-ENVIA PARA LIXEIRA
	public String cancelar(@PathVariable Long id) {
		return solicitacaoService.cancelarSolicitacao(id);
	}
	
	@GetMapping("quantidade/andamento/{id}")
	public int quantidadeAndamentoFuncionario(@PathVariable Long id) {
		return solicitacaoService.quantidadeEmAndamentoPorFuncionario(id);
	}
}
