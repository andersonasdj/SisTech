package br.com.techgol.app.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoDadosEdicaoRapida;
import br.com.techgol.app.dto.DtoDadosRestauracao;
import br.com.techgol.app.dto.DtoDashboardCliente;
import br.com.techgol.app.dto.DtoDashboardFuncionarios;
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.dto.DtoRelatorioFuncionario;
import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
import br.com.techgol.app.dto.DtoSolicitacaoFinalizada;
import br.com.techgol.app.dto.DtoSolicitacaoProjecaoCompleta;
import br.com.techgol.app.dto.DtoSolicitacaoRelatorios;
import br.com.techgol.app.dto.DtoSolicitacoesRelatorioFuncionario;
import br.com.techgol.app.dto.dashboard.DtoDashboard;
import br.com.techgol.app.dto.dashboard.DtoDashboardResumoFuncionario;
import br.com.techgol.app.email.EnviaSolicitacaoCriada;
import br.com.techgol.app.model.ConfiguracaoEmail;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.LogSolicitacao;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Agendamentos;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.DtoUltimaAtualizada;
import br.com.techgol.app.orm.PojecaoResumidaFinalizados;
import br.com.techgol.app.orm.ProjecaoDadosImpressao;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;
import br.com.techgol.app.repository.LogSolicitacaoRepository;
import br.com.techgol.app.repository.PesoSolicitacoes;
import br.com.techgol.app.repository.SolicitacaoRepository;
import jakarta.transaction.Transactional;

@Service
public class SolicitacaoService {
	
	@Autowired
	private SolicitacaoRepository repository;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private ConfiguracaoEmailService configuracaoEmailService;
	
	@Autowired
	EnviaSolicitacaoCriada envia;
	
	@Autowired
	private LogSolicitacaoRepository logSolicitacaoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private PesoSolicitacoes pesoSolicitacoes;
	
	@Transactional
	public void recalcularPesoSolicitacoes() {
		
		List<Solicitacao> solicitacoesAbertas = repository.buscaSolicitacoesAbertas();
		List<Solicitacao> solicitacoesAgendadas = repository.buscaSolicitacoesAgendadas();
		List<Solicitacao> solicitacoesPausadasAguardando = repository.buscaSolicitacoesPausadoAguardando();
		
		if(solicitacoesAbertas != null) {
			solicitacoesAbertas.forEach(s -> {
				s.setPeso(recalcularPeso(s));
			});
		}
		if(solicitacoesAgendadas != null) {
			solicitacoesAgendadas.forEach(s -> {
				s.setPeso(recalcularPeso(s));
			});
		}
		
		if(solicitacoesPausadasAguardando != null) {
			solicitacoesPausadasAguardando.forEach(s -> {
				s.setPeso(calcularPeso(s));
			});
		}
	}
	
	public Long recalcularPeso(Solicitacao s) {
		Long peso = calcularPeso(s);
		
		LocalDateTime dataHoje = LocalDateTime.now();
		Duration diferenca;
		
		if(s.getStatus().equals(Status.ABERTO)) {
			diferenca = Duration.between(s.getDataAbertura(), dataHoje);
			peso += (diferenca.toHours())/2;
		}
		
		if(s.getStatus().equals(Status.AGENDADO)) {
			diferenca = Duration.between(s.getDataAgendado(), dataHoje);
			if(diferenca.toMinutes() > 0) {
				peso += (diferenca.toMinutes())/2;
			}
		}
		
		return peso;
	}
	
	public Long calcularPeso(Solicitacao s) {
		Long peso=0l;
		
		if(clienteService.verificaSeVip(s.getCliente().getId()) == true) {
			peso += pesoSolicitacoes.findPesoByItem("VIP").getPeso();
		}
		
		if(clienteService.verificaSeRedFlag(s.getCliente().getId()) == true) {
			peso += pesoSolicitacoes.findPesoByItem("REDFLAG").getPeso();
		}
		
		if(s.getClassificacao().toString().equals("INCIDENTE")) {
			peso += pesoSolicitacoes.findPesoByItem("INCIDENTE").getPeso();
		}
		if(s.getClassificacao().toString().equals("PROBLEMA")) {
			peso += pesoSolicitacoes.findPesoByItem("PROBLEMA").getPeso();
		}
		if(s.getClassificacao().toString().equals("SOLICITACAO")) {
			peso += pesoSolicitacoes.findPesoByItem("SOLICITACAO").getPeso();
		}
		if(s.getClassificacao().toString().equals("ACESSO")) {
			peso += pesoSolicitacoes.findPesoByItem("ACESSO").getPeso();
		}
		if(s.getClassificacao().toString().equals("EVENTO")) {
			peso += pesoSolicitacoes.findPesoByItem("EVENTO").getPeso();
		}
		if(s.getClassificacao().toString().equals("BACKUP")) {
			peso += pesoSolicitacoes.findPesoByItem("BACKUP").getPeso();
		}
		
		if(s.getPrioridade().toString().equals("CRITICA")) {
			peso += pesoSolicitacoes.findPesoByItem("CRITICA").getPeso();
		}
		if(s.getPrioridade().toString().equals("ALTA")) {
			peso += pesoSolicitacoes.findPesoByItem("ALTA").getPeso();
		}
		if(s.getPrioridade().toString().equals("MEDIA")) {
			peso += pesoSolicitacoes.findPesoByItem("MEDIA").getPeso();
		}
		if(s.getPrioridade().toString().equals("BAIXA")) {
			peso += pesoSolicitacoes.findPesoByItem("BAIXA").getPeso();
		}
		if(s.getPrioridade().toString().equals("PLANEJADA")) {
			peso += pesoSolicitacoes.findPesoByItem("PLANEJADA").getPeso();
		}
		return peso;
	}
	
	
	@Transactional
	public int pausaSolicitacoesEmAndamento() {
		
		List<Solicitacao> solicitacaos = repository.buscaSolicitacoesEmAndamento();
		
		solicitacaos.forEach(s -> {
			
			s.setDataFinalizado(LocalDateTime.now().withNano(0));
			if(s.getDuracao() == null) {
				if(Duration.between(s.getDataAndamento(), LocalDateTime.now()).toMinutes() < 15) {
					s.setDuracao(15l);
				}else {
					s.setDuracao(Duration.between(s.getDataAndamento(), LocalDateTime.now()).toMinutes());
				}
			}else {
				Long tempoAnterior = s.getDuracao();
				s.setDuracao(Duration.between(s.getDataAndamento(), LocalDateTime.now()).toMinutes() + tempoAnterior);
			}
			
			s.setDataAtualizacao(LocalDateTime.now().withNano(0));
			s.setStatus(Status.PAUSADO);
			s.setDataAndamento(null);
			if(s.getLog() != null) {
				LogSolicitacao log = logSolicitacaoRepository.getReferenceById(s.getLog().getId());
				
				if(log != null) {
					String complementoLog = log.getLog() + " * Solicitação pausada pelo sistema às " + LocalTime.now().withNano(0) + " \n"
							+ s.geraLog("Sistema");
					log.setLog(complementoLog);
					logSolicitacaoRepository.save(log);
				}
			}
		});
		return solicitacaos.size();
	}
	
	public List<SolicitacaoProjecaoEntidadeComAtributos> buscaAgendamentosDoDia(LocalDateTime data) {
		LocalDateTime inicio = LocalDateTime.of(data.getYear(), data.getMonth(), data.getDayOfMonth(), 00, 00, 00);
		LocalDateTime fim = LocalDateTime.of(data.getYear(), data.getMonth(), data.getDayOfMonth(), 23, 59, 59);
		return repository.listarSolicitacoesAgendadasDoDia(Status.AGENDADO, false, inicio, fim);
	}
	
	public List<SolicitacaoProjecaoEntidadeComAtributos> buscaAgendamentosAtrasados() {
		return repository.listarSolicitacoesAgendadasAtrasadas(Status.AGENDADO, false, LocalDateTime.now());
	}
	
	public Long buscaAgendamentosAtrasadosQtd() {
		return repository.listarSolicitacoesAgendadasAtrasadasQtd(Status.AGENDADO, false, LocalDateTime.now());
	}
	
	public Long buscaAgendamentosHojeQtd() {
		LocalDate hoje = LocalDate.now();
		LocalDateTime inicio, fim;
		inicio = hoje.atTime(00, 00, 00);
		fim = hoje.atTime(23, 59, 59);
		return repository.listarSolicitacoesAgendadasHojeQtd(Status.AGENDADO, false, inicio, fim);
	}
	
	public Long buscaAtualizadosHojeQtd() {
		LocalDate hoje = LocalDate.now();
		LocalDateTime inicio, fim;
		inicio = hoje.atTime(00, 00, 00);
		fim = hoje.atTime(23, 59, 59);
		return repository.listarSolicitacoesAtualizadasHojeQtd(false, inicio, fim);
	}
	
	public Long buscaAbertosHojeQtd() {
		LocalDate hoje = LocalDate.now();
		LocalDateTime inicio, fim;
		inicio = hoje.atTime(00, 00, 00);
		fim = hoje.atTime(23, 59, 59);
		return repository.listarSolicitacoesAbertasHojeQtd(false, inicio, fim);
	}
	
	public Long buscaFinalizadosHojeQtd() {
		LocalDate hoje = LocalDate.now();
		LocalDateTime inicio, fim;
		inicio = hoje.atTime(00, 00, 00);
		fim = hoje.atTime(23, 59, 59);
		return repository.listarSolicitacoesFinalizadasHojeQtd(Status.FINALIZADO.toString(), false, inicio, fim);
	}
	
	
	@Transactional
	public String exclusaoLogigaSolicitacao(Long id) {
		
		if(repository.existsById(id)) {
			Solicitacao solicitacao = repository.getReferenceById(id);
			solicitacao.setStatus(Status.EXCLUIDO);
			solicitacao.setDataAtualizacao(LocalDateTime.now().withNano(0));
			solicitacao.setExcluido(true);
			solicitacao.setDataAgendado(null);
			solicitacao.setDataAndamento(null);
			return "Excluido com sucesso!";
		}else {
			return "Não foi possível excluir";
		}
	}
	
	@Transactional
	public String cancelarSolicitacao(Long id) {
		
		if(repository.existsById(id)) {
			Solicitacao solicitacao = repository.getReferenceById(id);
			solicitacao.setStatus(Status.FINALIZADO);
			solicitacao.setDataAtualizacao(LocalDateTime.now().withNano(0));
			solicitacao.setDataAgendado(null);
			solicitacao.setDataAndamento(null);
			solicitacao.setDataFinalizado(LocalDateTime.now().withNano(0));
			solicitacao.setDuracao(0l);
			String descricao = solicitacao.getDescricao();
			solicitacao.setDescricao("SOLICITAÇÃO CANCELADA!\n\n" + descricao);
			return "Cancelado com sucesso!";
		}else {
			return "Não foi possível cancelar";
		}
	}
	
	@Transactional
	public DtoSolicitacaoComFuncionario edicaoFinalizada(DtoSolicitacaoFinalizada dados) {
		Solicitacao solicitacao = repository.getReferenceById(dados.id());
		
		solicitacao.setLocal(dados.local());
		solicitacao.setCategoria(dados.categoria());
		
		if(dados.formaAbertura() != null) {
			solicitacao.setFormaAbertura(dados.formaAbertura());
		}
		
		solicitacao.setDescricao(dados.descricao());
		solicitacao.setObservacao(dados.observacao());
		solicitacao.setPrioridade(dados.prioridade());
		solicitacao.setResolucao(dados.resolucao());
		
		
		if(dados.solicitante() != null) {
			solicitacao.setSolicitante(dados.solicitante());
		}
		if(dados.afetado() != null) {
			solicitacao.setAfetado(dados.afetado());
		}
		
		solicitacao.setClassificacao(dados.classificacao());
		solicitacao.setDataAtualizacao(LocalDateTime.now().withNano(0));
		solicitacao.setDataAbertura(LocalDateTime.parse(dados.dataAbertura()+"T"+dados.horaAbertura()));
		
		Funcionario funcionario = funcionarioService.buscaPorNome(dados.nomeFuncionario());
		solicitacao.setFuncionario(funcionario);

		boolean andamento = false;
		boolean finalizado = false;
		
		String dataHoraAndamento, dataHoraFinalizado;
		dataHoraAndamento = LocalDateTime.parse(dados.dataAndamento()+"T"+dados.horaAndamento()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		dataHoraFinalizado = LocalDateTime.parse(dados.dataFinalizado()+"T"+dados.horaFinalizado()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		
		if(!solicitacao.getDataAndamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")).equals(dataHoraAndamento)) {
			solicitacao.setDataAndamento(LocalDateTime.parse(dados.dataAndamento()+"T"+dados.horaAndamento()));
			andamento = true;
		}
		if(!solicitacao.getDataFinalizado().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")).equals(dataHoraFinalizado)){
			solicitacao.setDataFinalizado(LocalDateTime.parse(dados.dataFinalizado()+"T"+dados.horaFinalizado()));
			finalizado = true;
		}
		if(andamento || finalizado) {
			solicitacao.setDuracao(Duration.between(solicitacao.getDataAndamento(), solicitacao.getDataFinalizado()).toMinutes());
		}
		
		String funcionarioBase = (((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		if(solicitacao.getLog() != null) {
			LogSolicitacao log = logSolicitacaoRepository.getReferenceById(solicitacao.getLog().getId());
			String conteudo = log.getLog() + " * Atualização de solicitação finalizada. \n" + solicitacao.geraLog(funcionarioBase);
			log.setLog(conteudo);
			logSolicitacaoRepository.save(log);
		}else {
			LogSolicitacao log = new LogSolicitacao(solicitacao);
			logSolicitacaoRepository.save(log);
			solicitacao.setLog(log);
		}
		
		return new DtoSolicitacaoComFuncionario(solicitacao);
		
	}
	
	public Solicitacao edicaoRapida(DtoDadosEdicaoRapida dados) {
		
		Solicitacao solicitacao = repository.getReferenceById(dados.id());
		
		if(dados.nomeFuncionario() != null && dados.nomeFuncionario() != "") {
			if(funcionarioService.existePorNomeFuncionario(dados.nomeFuncionario())) {
				Funcionario funcionario = funcionarioService.buscaPorNome(dados.nomeFuncionario());
				solicitacao.setFuncionario(funcionario);
			}
		}
		if(dados.status().equals(Status.ANDAMENTO) && !solicitacao.getStatus().equals(Status.ANDAMENTO)) {
			
			solicitacao.setDataAndamento(LocalDateTime.now().withNano(0));
			solicitacao.setDataAgendado(null);
		}
		if(dados.status().equals(Status.ABERTO) || dados.status().equals(Status.AGENDADO)) {
			
			if(dados.status().equals(Status.ABERTO)) {
				solicitacao.setDataAndamento(null);
				solicitacao.setDuracao(0l); 
				solicitacao.setDataFinalizado(null);
			}
			
			if(dados.dataAgendado() != null) {
				if(!dados.dataAgendado().isBlank() || !dados.dataAgendado().isEmpty()) {
					solicitacao.setDataAgendado(LocalDateTime.parse(dados.dataAgendado()+"T"+dados.horaAgendado()));
					
					if(solicitacao.getDataAndamento() != null) {
						solicitacao.setDuracao(solicitacao.getDuracao() + Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes());
					}
					solicitacao.setDataAndamento(null);
				}
			}
			
		}
		if(dados.status().equals(Status.AGUARDANDO) || dados.status().equals(Status.PAUSADO)) {
			
			if(solicitacao.getDataAndamento() != null) {
				if(solicitacao.getDuracao() == null) {
					solicitacao.setDuracao(0l);
				}
				solicitacao.setDuracao(solicitacao.getDuracao() + Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes());
			}
			
			solicitacao.setDataAndamento(null);
			solicitacao.setDataAgendado(null);
			solicitacao.setDataFinalizado(null);
			
		}
		if(dados.status().equals(Status.FINALIZADO)) {
			solicitacao.setDataFinalizado(LocalDateTime.now().withNano(0));
			if(solicitacao.getDuracao() != null) {
				if(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes() + solicitacao.getDuracao() < 15) {
					solicitacao.setDuracao(15l);
				}else {
					Long tempoAnterior = solicitacao.getDuracao();
					solicitacao.setDuracao(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes() + tempoAnterior);
				}
			}else {
				solicitacao.setDuracao(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes());
			}
			
		}
		if(solicitacao.getStatus().equals(Status.EXCLUIDO) && dados.status().equals(Status.ABERTO)) {
			solicitacao.setExcluido(false);
			solicitacao.setDataAndamento(null);
		}
		solicitacao.setDataAtualizacao(LocalDateTime.now().withNano(0));
		solicitacao.setStatus(dados.status());
		solicitacao.setDescricao(dados.descricao().trim());
		solicitacao.setResolucao(dados.resolucao().trim());
		solicitacao.setObservacao(dados.observacao().trim());
		solicitacao.setCategoria(dados.categoria());
		solicitacao.setClassificacao(dados.classificacao());
		solicitacao.setPrioridade(dados.prioridade());
		solicitacao.setLocal(dados.local());
		solicitacao.setSolicitante(dados.solicitante());
		solicitacao.setAfetado(dados.afetado());
		solicitacao.setFormaAbertura(dados.formaAbertura());

		String funcionarioBase = (((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		if(solicitacao.getLog() != null) {
			LogSolicitacao log = logSolicitacaoRepository.getReferenceById(solicitacao.getLog().getId());
			String conteudo = log.getLog() + solicitacao.geraLog(funcionarioBase);
			log.setLog(conteudo);
			logSolicitacaoRepository.save(log);
		}else {
			LogSolicitacao log = new LogSolicitacao(solicitacao);
			logSolicitacaoRepository.save(log);
			solicitacao.setLog(log);
		}
		solicitacao.setVersao(solicitacao.getVersao()+1);
		solicitacao.setPeso(calcularPeso(solicitacao));
		return repository.save(solicitacao);
	}
	
	public ProjecaoDadosImpressao impressao(Long id) {
		
		return repository.impressaoPorId(id);
		
	}

	public List<Solicitacao> buscarTodos() {
		return repository.findAll();
	}

	public Page<SolicitacaoProjecao> listarSolicitacoes(Pageable page, String status, Boolean exluida) {
		return repository.listarSolicitacoes(page, status, exluida);
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesPorStatus(Pageable page, String status, Boolean exluida) {
		return repository.listarSolicitacoesPorStatus(page, status, exluida);
	}
	
	public List<SolicitacaoProjecao> listarSolicitacoesFinalizadas(String status, Boolean exluida) {
		return repository.listarSolicitacoesFinalizadas(status, exluida);
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadasPorCliente(Pageable page, Long id) {
		return repository.listarSolicitacoesFinalizadasPorCliente(page, id);
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadasPorFuncionario(Pageable page, Long id) {
		return repository.listarSolicitacoesFinalizadasPorFuncionario(page, id);
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesNaoFinalizadasPorCliente(Pageable page, Long id) {
		return repository.listarSolicitacoesNaoFinalizadasPorCliente(page, id);
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesNaoFinalizadasPorFuncionario(Pageable page, Long id) {
		return repository.listarSolicitacoesNaoFinalizadasPorFuncionario(page, id);
	}

	public Solicitacao buscarPorId(Long id) {
		return repository.getReferenceById(id);
	}

	public DtoSolicitacaoComFuncionario salvarNovaSolicitacao(Solicitacao solicitacao) {
		Funcionario funcionarioBase = funcionarioService.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		
		ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.ABERTURA.toString());
		
		solicitacao.setAbertoPor(funcionarioBase.getNomeFuncionario());
		if(solicitacao.getStatus() == Status.ANDAMENTO) {
			solicitacao.setDataAndamento(LocalDateTime.now().withNano(0));
		}
		
		LogSolicitacao log = logSolicitacaoRepository.save(new LogSolicitacao(solicitacao));
		
		solicitacao.setLog(log);
		
		solicitacao.setPeso(calcularPeso(solicitacao));
		solicitacao.setVersao(0);
		
		DtoSolicitacaoComFuncionario dados = new DtoSolicitacaoComFuncionario(repository.save(solicitacao));
		
		
		if(config.isStatus() && !config.getEmail().isEmpty()) {
			envia.enviar(dados); // ### ENVIA EMAIL NO CADASTRO
		}else {
			System.out.println("Sem configuração de email de abertura habilitado");
		}
		
		return dados;
	}
	
	public DtoDashboardCliente geraDashboardCliente(Long id) {
		
		int onsite,offsite,problema,incidente,solicitacao,backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,pausado,finalizado,totalSolicitacoes;
		int totalMesCorrente, email, telefone, local, whatsapp, proativo;
		Long totalMinutosMes=0l;
		LocalDate dataPesquisa = LocalDate.now().plusDays(-LocalDate.now().getDayOfMonth()).plusDays(1);
		LocalDateTime dataDePesquisa;
		dataDePesquisa = dataPesquisa.atTime(00, 00, 00);
		
		onsite = repository.totalPorLocalPorCliente(id, Local.ONSITE.toString(), false);
		offsite = repository.totalPorLocalPorCliente(id, Local.OFFSITE.toString(), false);
		email = repository.totalPorFormaAberturaPorCliente(id, FormaAbertura.EMAIL.toString(), false);
		telefone = repository.totalPorFormaAberturaPorCliente(id, FormaAbertura.TELEFONE.toString(), false);
		local = repository.totalPorFormaAberturaPorCliente(id, FormaAbertura.LOCAL.toString(), false);
		whatsapp = repository.totalPorFormaAberturaPorCliente(id, FormaAbertura.WHATSAPP.toString(), false);
		proativo = repository.totalPorFormaAberturaPorCliente(id, FormaAbertura.PROATIVO.toString(), false);
		problema = repository.totalPorClassificacaoPorCliente(id, Classificacao.PROBLEMA.toString(), false);
		incidente = repository.totalPorClassificacaoPorCliente(id, Classificacao.INCIDENTE.toString(), false);
		solicitacao = repository.totalPorClassificacaoPorCliente(id, Classificacao.SOLICITACAO.toString(), false);
		backup = repository.totalPorClassificacaoPorCliente(id, Classificacao.BACKUP.toString(), false);
		acesso = repository.totalPorClassificacaoPorCliente(id, Classificacao.ACESSO.toString(), false);
		evento = repository.totalPorClassificacaoPorCliente(id, Classificacao.EVENTO.toString(), false);
		baixa = repository.totalPorPrioridadePorCliente(id, Prioridade.BAIXA.toString(), false);
		media = repository.totalPorPrioridadePorCliente(id, Prioridade.MEDIA.toString(), false);
		alta = repository.totalPorPrioridadePorCliente(id, Prioridade.ALTA.toString(), false);
		critica = repository.totalPorPrioridadePorCliente(id, Prioridade.CRITICA.toString(), false);
		planejada = repository.totalPorPrioridadePorCliente(id, Prioridade.PLANEJADA.toString(), false);
		aberto = repository.countByClienteIdAndStatusAndExcluido(id, Status.ABERTO, false);
		andamento = repository.countByClienteIdAndStatusAndExcluido(id, Status.ANDAMENTO, false);
		agendado = repository.countByClienteIdAndStatusAndExcluido(id, Status.AGENDADO, false);
		aguardando = repository.countByClienteIdAndStatusAndExcluido(id, Status.AGUARDANDO, false);
		pausado = repository.countByClienteIdAndStatusAndExcluido(id, Status.PAUSADO, false);
		finalizado = repository.countByClienteIdAndStatusAndExcluido(id, Status.FINALIZADO, false);
		totalSolicitacoes = aberto+andamento+agendado+aguardando+pausado+finalizado;
		totalMesCorrente = repository.countByClienteIdAndExcluidoAndDataFinalizadoAfter(id, false, dataDePesquisa);
		
		List<PojecaoResumidaFinalizados> solicitacoes = repository.findByClienteIdAndExcluidoAndStatusAndDataFinalizadoAfter(id, false, Status.FINALIZADO, dataDePesquisa);;
		
		for (PojecaoResumidaFinalizados s : solicitacoes) {
			if(s.getDuracao() != null) {
				totalMinutosMes += s.getDuracao();
			}
		}
		return new DtoDashboardCliente(onsite,offsite,problema,incidente,solicitacao,
				backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,
				agendado,aguardando,pausado,finalizado,totalSolicitacoes, totalMesCorrente, 
				totalMinutosMes, email, telefone, local, whatsapp, proativo);
	}
	
	public DtoDashboardCliente geraDashboardFuncionario(Long id) {
		
		int onsite,offsite,problema,incidente,solicitacao,backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,pausado,finalizado,totalSolicitacoes;
		int totalMesCorrente, email, telefone, local, whatsapp, proativo;
		Long totalMinutosMes=0l;
		LocalDate dataPesquisa = LocalDate.now().plusDays(-LocalDate.now().getDayOfMonth()).plusDays(1);
		LocalDateTime dataDePesquisa;
		dataDePesquisa = dataPesquisa.atTime(00, 00, 00);
		
		onsite = repository.totalPorLocalPorFuncionario(id, Local.ONSITE.toString(), false);
		offsite = repository.totalPorLocalPorFuncionario(id, Local.OFFSITE.toString(), false);
		email = repository.totalPorFormaAberturaPorFuncionario(id, FormaAbertura.EMAIL.toString(), false);
		telefone = repository.totalPorFormaAberturaPorFuncionario(id, FormaAbertura.TELEFONE.toString(), false);
		local = repository.totalPorFormaAberturaPorFuncionario(id, FormaAbertura.LOCAL.toString(), false);
		whatsapp = repository.totalPorFormaAberturaPorFuncionario(id, FormaAbertura.WHATSAPP.toString(), false);
		proativo = repository.totalPorFormaAberturaPorFuncionario(id, FormaAbertura.PROATIVO.toString(), false);
		problema = repository.totalPorClassificacaoPorFuncionario(id, Classificacao.PROBLEMA.toString(), false);
		incidente = repository.totalPorClassificacaoPorFuncionario(id, Classificacao.INCIDENTE.toString(), false);
		solicitacao = repository.totalPorClassificacaoPorFuncionario(id, Classificacao.SOLICITACAO.toString(), false);
		backup = repository.totalPorClassificacaoPorFuncionario(id, Classificacao.BACKUP.toString(), false);
		acesso = repository.totalPorClassificacaoPorFuncionario(id, Classificacao.ACESSO.toString(), false);
		evento = repository.totalPorClassificacaoPorFuncionario(id, Classificacao.EVENTO.toString(), false);
		baixa = repository.totalPorPrioridadePorFuncionario(id, Prioridade.BAIXA.toString(), false);
		media = repository.totalPorPrioridadePorFuncionario(id, Prioridade.MEDIA.toString(), false);
		alta = repository.totalPorPrioridadePorFuncionario(id, Prioridade.ALTA.toString(), false);
		critica = repository.totalPorPrioridadePorFuncionario(id, Prioridade.CRITICA.toString(), false);
		planejada = repository.totalPorPrioridadePorFuncionario(id, Prioridade.PLANEJADA.toString(), false);
		aberto = repository.countByFuncionarioIdAndStatusAndExcluido(id, Status.ABERTO, false);
		andamento = repository.countByFuncionarioIdAndStatusAndExcluido(id, Status.ANDAMENTO, false);
		agendado = repository.countByFuncionarioIdAndStatusAndExcluido(id, Status.AGENDADO, false);
		aguardando = repository.countByFuncionarioIdAndStatusAndExcluido(id, Status.AGUARDANDO, false);
		pausado = repository.countByFuncionarioIdAndStatusAndExcluido(id, Status.PAUSADO, false);
		finalizado = repository.countByFuncionarioIdAndStatusAndExcluido(id, Status.FINALIZADO, false);
		totalSolicitacoes = aberto+andamento+agendado+aguardando+pausado+finalizado;
		totalMesCorrente = repository.countByFuncionarioIdAndExcluidoAndDataFinalizadoAfter(id, false, dataDePesquisa);
		
		List<PojecaoResumidaFinalizados> solicitacoes = repository.findByFuncionarioIdAndExcluidoAndStatusAndDataFinalizadoAfter(id, false, Status.FINALIZADO, dataDePesquisa);;
		
		for (PojecaoResumidaFinalizados s : solicitacoes) {
			if(s.getDuracao() != null) {
				totalMinutosMes += s.getDuracao();
			}
		}
		return new DtoDashboardCliente(onsite,offsite,problema,incidente,solicitacao,
				backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,
				agendado,aguardando,pausado,finalizado,totalSolicitacoes, totalMesCorrente, 
				totalMinutosMes, email, telefone, local, whatsapp, proativo);
	}
	
	public DtoDashboardFuncionarios geraDashboardFuncionarioPorPeriodo(Long id, String periodo, LocalDate ini, LocalDate termino) {
		LocalDateTime inicio, fim;
		
		int onsite,offsite,problema,incidente,solicitacao,backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,pausado,finalizado,totalSolicitacoes;
		int totalMesCorrente, email, telefone, local, whatsapp, proativo;
		Long totalMinutosMes=0l;
		List<PojecaoResumidaFinalizados> solicitacoes;
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			
			if(periodo.equals("abertura")) {
				onsite = repository.totalPorLocalPorFuncionarioPeridoDataAbertura(id, Local.ONSITE.toString(), false, inicio, fim);
				offsite = repository.totalPorLocalPorFuncionarioPeridoDataAbertura(id, Local.OFFSITE.toString(), false, inicio, fim);
				email = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAbertura(id, FormaAbertura.EMAIL.toString(), false, inicio, fim);
				telefone = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAbertura(id, FormaAbertura.TELEFONE.toString(), false, inicio, fim);
				local = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAbertura(id, FormaAbertura.LOCAL.toString(), false, inicio, fim);
				whatsapp = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAbertura(id, FormaAbertura.WHATSAPP.toString(), false, inicio, fim);
				proativo = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAbertura(id, FormaAbertura.PROATIVO.toString(), false, inicio, fim);
				problema = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAbertura(id, Classificacao.PROBLEMA.toString(), false, inicio, fim);
				incidente = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAbertura(id, Classificacao.INCIDENTE.toString(), false, inicio, fim);
				solicitacao = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAbertura(id, Classificacao.SOLICITACAO.toString(), false, inicio, fim);
				backup = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAbertura(id, Classificacao.BACKUP.toString(), false, inicio, fim);
				acesso = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAbertura(id, Classificacao.ACESSO.toString(), false, inicio, fim);
				evento = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAbertura(id, Classificacao.EVENTO.toString(), false, inicio, fim);
				baixa = repository.totalPorPrioridadePorFuncionarioPeriodoDataAbertura(id, Prioridade.BAIXA.toString(), false, inicio, fim);
				media = repository.totalPorPrioridadePorFuncionarioPeriodoDataAbertura(id, Prioridade.MEDIA.toString(), false, inicio, fim);
				alta = repository.totalPorPrioridadePorFuncionarioPeriodoDataAbertura(id, Prioridade.ALTA.toString(), false, inicio, fim);
				critica = repository.totalPorPrioridadePorFuncionarioPeriodoDataAbertura(id, Prioridade.CRITICA.toString(), false, inicio, fim);
				planejada = repository.totalPorPrioridadePorFuncionarioPeriodoDataAbertura(id, Prioridade.PLANEJADA.toString(), false, inicio, fim);
				aberto = repository.totalPorStatusPorFuncionarioPeriodoDataAbertura(id, Status.ABERTO.toString(), false, inicio, fim);
				andamento = repository.totalPorStatusPorFuncionarioPeriodoDataAbertura(id, Status.ANDAMENTO.toString(), false, inicio, fim);
				agendado = repository.totalPorStatusPorFuncionarioPeriodoDataAbertura(id, Status.AGENDADO.toString(), false, inicio, fim);
				aguardando = repository.totalPorStatusPorFuncionarioPeriodoDataAbertura(id, Status.AGUARDANDO.toString(), false, inicio, fim);
				pausado = repository.totalPorStatusPorFuncionarioPeriodoDataAbertura(id, Status.PAUSADO.toString(), false, inicio, fim);
				finalizado = repository.totalPorStatusPorFuncionarioPeriodoDataAbertura(id, Status.FINALIZADO.toString(), false, inicio, fim);
				totalSolicitacoes = aberto+andamento+agendado+aguardando+pausado+finalizado;
				totalMesCorrente = repository.totalPorFuncionarioPeriodoDataAbertura(id, false, inicio, fim);
				solicitacoes = repository.listaSolicitacoesPorFuncionarioPorPeriodoAbertura(id, false, inicio, fim );

			}else if(periodo.equals("fechamento")) {
				onsite = repository.totalPorLocalPorFuncionarioPeridoDataFinalizado(id, Local.ONSITE.toString(), false, inicio, fim);
				offsite = repository.totalPorLocalPorFuncionarioPeridoDataFinalizado(id, Local.OFFSITE.toString(), false, inicio, fim);
				email = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataFinalizado(id, FormaAbertura.EMAIL.toString(), false, inicio, fim);
				telefone = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataFinalizado(id, FormaAbertura.TELEFONE.toString(), false, inicio, fim);
				local = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataFinalizado(id, FormaAbertura.LOCAL.toString(), false, inicio, fim);
				whatsapp = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataFinalizado(id, FormaAbertura.WHATSAPP.toString(), false, inicio, fim);
				proativo = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataFinalizado(id, FormaAbertura.PROATIVO.toString(), false, inicio, fim);
				problema = repository.totalPorClassificacaoPorFuncionarioPeriodoDataFinalizado(id, Classificacao.PROBLEMA.toString(), false, inicio, fim);
				incidente = repository.totalPorClassificacaoPorFuncionarioPeriodoDataFinalizado(id, Classificacao.INCIDENTE.toString(), false, inicio, fim);
				solicitacao = repository.totalPorClassificacaoPorFuncionarioPeriodoDataFinalizado(id, Classificacao.SOLICITACAO.toString(), false, inicio, fim);
				backup = repository.totalPorClassificacaoPorFuncionarioPeriodoDataFinalizado(id, Classificacao.BACKUP.toString(), false, inicio, fim);
				acesso = repository.totalPorClassificacaoPorFuncionarioPeriodoDataFinalizado(id, Classificacao.ACESSO.toString(), false, inicio, fim);
				evento = repository.totalPorClassificacaoPorFuncionarioPeriodoDataFinalizado(id, Classificacao.EVENTO.toString(), false, inicio, fim);
				baixa = repository.totalPorPrioridadePorFuncionarioPeriodoDataFinalizado(id, Prioridade.BAIXA.toString(), false, inicio, fim);
				media = repository.totalPorPrioridadePorFuncionarioPeriodoDataFinalizado(id, Prioridade.MEDIA.toString(), false, inicio, fim);
				alta = repository.totalPorPrioridadePorFuncionarioPeriodoDataFinalizado(id, Prioridade.ALTA.toString(), false, inicio, fim);
				critica = repository.totalPorPrioridadePorFuncionarioPeriodoDataFinalizado(id, Prioridade.CRITICA.toString(), false, inicio, fim);
				planejada = repository.totalPorPrioridadePorFuncionarioPeriodoDataFinalizado(id, Prioridade.PLANEJADA.toString(), false, inicio, fim);
				aberto = repository.totalPorStatusPorFuncionarioPeriodoDataFinalizado(id, Status.ABERTO.toString(), false, inicio, fim);
				andamento = repository.totalPorStatusPorFuncionarioPeriodoDataFinalizado(id, Status.ANDAMENTO.toString(), false, inicio, fim);
				agendado = repository.totalPorStatusPorFuncionarioPeriodoDataFinalizado(id, Status.AGENDADO.toString(), false, inicio, fim);
				aguardando = repository.totalPorStatusPorFuncionarioPeriodoDataFinalizado(id, Status.AGUARDANDO.toString(), false, inicio, fim);
				pausado = repository.totalPorStatusPorFuncionarioPeriodoDataFinalizado(id, Status.PAUSADO.toString(), false, inicio, fim);
				finalizado = repository.totalPorStatusPorFuncionarioPeriodoDataFinalizado(id, Status.FINALIZADO.toString(), false, inicio, fim);
				totalSolicitacoes = aberto+andamento+agendado+aguardando+pausado+finalizado;
				totalMesCorrente = repository.totalPorFuncionarioPeriodoDataFinalizado(id, false, inicio, fim);
				solicitacoes = repository.listaSolicitacoesPorFuncionarioPorPeriodoFinalizado(id, false, inicio, fim );

			}else {
				onsite = repository.totalPorLocalPorFuncionarioPeridoDataAtualizado(id, Local.ONSITE.toString(), false, inicio, fim);
				offsite = repository.totalPorLocalPorFuncionarioPeridoDataAtualizado(id, Local.OFFSITE.toString(), false, inicio, fim);
				email = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAtualizado(id, FormaAbertura.EMAIL.toString(), false, inicio, fim);
				telefone = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAtualizado(id, FormaAbertura.TELEFONE.toString(), false, inicio, fim);
				local = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAtualizado(id, FormaAbertura.LOCAL.toString(), false, inicio, fim);
				whatsapp = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAtualizado(id, FormaAbertura.WHATSAPP.toString(), false, inicio, fim);
				proativo = repository.totalPorFormaAberturaPorFuncionarioPeriodoDataAtualizado(id, FormaAbertura.PROATIVO.toString(), false, inicio, fim);
				problema = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAtualizado(id, Classificacao.PROBLEMA.toString(), false, inicio, fim);
				incidente = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAtualizado(id, Classificacao.INCIDENTE.toString(), false, inicio, fim);
				solicitacao = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAtualizado(id, Classificacao.SOLICITACAO.toString(), false, inicio, fim);
				backup = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAtualizado(id, Classificacao.BACKUP.toString(), false, inicio, fim);
				acesso = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAtualizado(id, Classificacao.ACESSO.toString(), false, inicio, fim);
				evento = repository.totalPorClassificacaoPorFuncionarioPeriodoDataAtualizado(id, Classificacao.EVENTO.toString(), false, inicio, fim);
				baixa = repository.totalPorPrioridadePorFuncionarioPeriodoDataAtualizado(id, Prioridade.BAIXA.toString(), false, inicio, fim);
				media = repository.totalPorPrioridadePorFuncionarioPeriodoDataAtualizado(id, Prioridade.MEDIA.toString(), false, inicio, fim);
				alta = repository.totalPorPrioridadePorFuncionarioPeriodoDataAtualizado(id, Prioridade.ALTA.toString(), false, inicio, fim);
				critica = repository.totalPorPrioridadePorFuncionarioPeriodoDataAtualizado(id, Prioridade.CRITICA.toString(), false, inicio, fim);
				planejada = repository.totalPorPrioridadePorFuncionarioPeriodoDataAtualizado(id, Prioridade.PLANEJADA.toString(), false, inicio, fim);
				aberto = repository.totalPorStatusPorFuncionarioPeriodoDataAtualizado(id, Status.ABERTO.toString(), false, inicio, fim);
				andamento = repository.totalPorStatusPorFuncionarioPeriodoDataAtualizado(id, Status.ANDAMENTO.toString(), false, inicio, fim);
				agendado = repository.totalPorStatusPorFuncionarioPeriodoDataAtualizado(id, Status.AGENDADO.toString(), false, inicio, fim);
				aguardando = repository.totalPorStatusPorFuncionarioPeriodoDataAtualizado(id, Status.AGUARDANDO.toString(), false, inicio, fim);
				pausado = repository.totalPorStatusPorFuncionarioPeriodoDataAtualizado(id, Status.PAUSADO.toString(), false, inicio, fim);
				finalizado = repository.totalPorStatusPorFuncionarioPeriodoDataAtualizado(id, Status.FINALIZADO.toString(), false, inicio, fim);
				totalSolicitacoes = aberto+andamento+agendado+aguardando+pausado+finalizado;
				totalMesCorrente = repository.totalPorFuncionarioPeriodoDataAtualizado(id, false, inicio, fim);
				solicitacoes = repository.listaSolicitacoesPorFuncionarioPorPeriodoAtualizado(id, false, inicio, fim );
			}
			
			for (PojecaoResumidaFinalizados s : solicitacoes) {
				if(s != null) {
					totalMinutosMes += s.getDuracao();
				}else {
					totalMinutosMes += 0;
				}
			}
			return new DtoDashboardFuncionarios(onsite,offsite,problema,incidente,solicitacao,
					backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,
					agendado,aguardando,pausado,finalizado,totalSolicitacoes,totalMinutosMes,
					totalMesCorrente, email, telefone, local, whatsapp, proativo);
			
		}else {
			return null;
		}
	}

	public DtoDashboard geraDashboard() {
		
		int onsite,offsite,problema,incidente,solicitacao,backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,pausado,totalSolicitacoes;
		int email, telefone, local, whatsapp, proativo;
		
		onsite = repository.totalPorLocal(Local.ONSITE.toString() , false, Status.FINALIZADO.toString());
		offsite = repository.totalPorLocal(Local.OFFSITE.toString() , false, Status.FINALIZADO.toString());
		email = repository.totalPorFormaAbertura(FormaAbertura.EMAIL.toString() , false, Status.FINALIZADO.toString());
		telefone = repository.totalPorFormaAbertura(FormaAbertura.TELEFONE.toString() , false, Status.FINALIZADO.toString());
		local = repository.totalPorFormaAbertura(FormaAbertura.LOCAL.toString() , false, Status.FINALIZADO.toString());
		whatsapp = repository.totalPorFormaAbertura(FormaAbertura.WHATSAPP.toString() , false, Status.FINALIZADO.toString());
		proativo = repository.totalPorFormaAbertura(FormaAbertura.PROATIVO.toString() , false, Status.FINALIZADO.toString());
		problema = repository.totalPorClassificacao(Classificacao.PROBLEMA.toString(), false, Status.FINALIZADO.toString());
		incidente = repository.totalPorClassificacao(Classificacao.INCIDENTE.toString(), false, Status.FINALIZADO.toString());
		solicitacao = repository.totalPorClassificacao(Classificacao.SOLICITACAO.toString(), false, Status.FINALIZADO.toString());
		backup = repository.totalPorClassificacao(Classificacao.BACKUP.toString(), false, Status.FINALIZADO.toString());
		acesso = repository.totalPorClassificacao(Classificacao.ACESSO.toString(), false, Status.FINALIZADO.toString());
		evento = repository.totalPorClassificacao(Classificacao.EVENTO.toString(), false, Status.FINALIZADO.toString());
		baixa = repository.totalPorPrioridade(Prioridade.BAIXA.toString(), false, Status.FINALIZADO.toString());
		media = repository.totalPorPrioridade(Prioridade.MEDIA.toString(), false, Status.FINALIZADO.toString());
		alta = repository.totalPorPrioridade(Prioridade.ALTA.toString(), false, Status.FINALIZADO.toString());
		critica = repository.totalPorPrioridade(Prioridade.CRITICA.toString(), false, Status.FINALIZADO.toString());
		planejada = repository.totalPorPrioridade(Prioridade.PLANEJADA.toString(), false, Status.FINALIZADO.toString());
		aberto = repository.countByStatusAndExcluido(Status.ABERTO, false);
		andamento = repository.countByStatusAndExcluido(Status.ANDAMENTO, false);
		agendado = repository.countByStatusAndExcluido(Status.AGENDADO, false);
		aguardando = repository.countByStatusAndExcluido(Status.AGUARDANDO, false);
		pausado = repository.countByStatusAndExcluido(Status.PAUSADO, false);
		totalSolicitacoes = aberto+andamento+agendado+aguardando+pausado;
		
		List<DtoListarFuncionarios> funcionarios = funcionarioService.listar();
		List<DtoDashboardResumoFuncionario> listaDto = new ArrayList<>();
		
		int totalFUncionarios = funcionarios.size();
		
		funcionarios.forEach(f -> {
			listaDto.add(new DtoDashboardResumoFuncionario(f.nomeFuncionario(),repository.totalPorFuncionario(f.id(), false, Status.FINALIZADO.toString())));
		});
		
		
		return new DtoDashboard(onsite,offsite,problema,incidente,solicitacao,
				backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,
				andamento,agendado,aguardando,pausado,totalSolicitacoes,totalFUncionarios,
				listaDto, email,telefone,local,whatsapp,proativo);
	}

	public DtoSolicitacaoRelatorios geraRelatorios() {
		
		List<Long> idFuncionarios = funcionarioService.listarIdFuncionarosLong();
		List<DtoSolicitacoesRelatorioFuncionario> relatorioFuncionario = new ArrayList<>();

		idFuncionarios.forEach(f -> {
			Long abertos, andamento, agendados, aguardando, pausado, total;
			abertos = repository.countByStatusAndFuncionarioId(Status.ABERTO, f);
			andamento = repository.countByStatusAndFuncionarioId(Status.ANDAMENTO, f);
			agendados = repository.countByStatusAndFuncionarioId(Status.AGENDADO, f);
			aguardando = repository.countByStatusAndFuncionarioId(Status.AGUARDANDO, f);
			pausado = repository.countByStatusAndFuncionarioId(Status.PAUSADO, f);
			total = abertos+andamento+agendados+aguardando+pausado;
			relatorioFuncionario.add(new DtoSolicitacoesRelatorioFuncionario(funcionarioService.buscaNomeFuncionarioPorId(f), abertos, andamento, agendados, aguardando, pausado, total));
		});
		return new DtoSolicitacaoRelatorios(buscaAbertosHojeQtd(),buscaFinalizadosHojeQtd(),buscaAgendamentosHojeQtd(), buscaAgendamentosAtrasadosQtd(), buscaAtualizadosHojeQtd(), relatorioFuncionario);
	}

	@Transactional
	public DtoDadosRestauracao restaurar(Long id) {
		Solicitacao solicitacao = repository.getReferenceById(id);
		solicitacao.setDataAtualizacao(LocalDateTime.now().withNano(0));
		solicitacao.setExcluido(false);
		solicitacao.setStatus(Status.ABERTO);
		solicitacao.setDataAndamento(null);
		solicitacao.setDataAgendado(null);
		return new DtoDadosRestauracao(solicitacao.getId());
	}

	public DtoSolicitacaoProjecaoCompleta buscarFinalizada(Long id) {
		return new DtoSolicitacaoProjecaoCompleta(repository.buscarSolicitacaoFinalizada(id, Status.FINALIZADO.toString()));
	}
	
	public DtoSolicitacaoProjecaoCompleta buscarSolicitacaoParaRelatorio(Long id) {
		return new DtoSolicitacaoProjecaoCompleta(repository.buscarSolicitacaoRelatorio(id));
	}

	public DtoUltimaAtualizada ultimaAtualizada() {
		
		return repository.buscaUltimaAtualizada();
	}
	
public Page<SolicitacaoProjecao> listarSolicitacoesPorPeriodo(Pageable page, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			return repository.listarSolicitacoesPorPeriodoRelatorio(page, false, inicio, fim);
		}else if(ini != null) {
			inicio = ini.atTime(00, 00, 00);
			fim = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 23, 59, 59);
			return repository.listarSolicitacoesPorPeriodoRelatorio(page, false, inicio, fim);
		} else {
			inicio = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
			return repository.listarSolicitacoesPorPeriodoRelatorio(page, false, inicio, inicio);
		}
			
	}

	public Page<SolicitacaoProjecao> listarSolicitacoesPorData(Pageable page, Long id, String periodo, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			
			
			if(periodo.equals("abertura")) {
				return repository.listarSolicitacoesPorDataRelatorioAbertura(page, id, false, inicio, fim);
			}else if(periodo.equals("fechamento")) {
				return repository.listarSolicitacoesPorDataRelatorioFechamento(page, id, false, inicio, fim);
			}else {
				return repository.listarSolicitacoesPorDataRelatorioAtualizado(page, id, false, inicio, fim);
			}
			
		}else if(ini != null) {
			
			inicio = ini.atTime(00, 00, 00);
			fim = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 23, 59, 59);
			
			if(periodo.equals("abertura")) {
				return repository.listarSolicitacoesPorDataRelatorioAbertura(page, id, false, inicio, fim);
			}else if(periodo.equals("fechamento")) {
				return repository.listarSolicitacoesPorDataRelatorioFechamento(page, id, false, inicio, fim);
			}else {
				return repository.listarSolicitacoesPorDataRelatorioAtualizado(page, id, false, inicio, fim);
			}
			
		} else {
			inicio = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
			
			if(periodo.equals("abertura")) {
				return repository.listarSolicitacoesPorDataRelatorioAbertura(page, id, false, inicio, inicio);
			}else if(periodo.equals("fechamento")) {
				return repository.listarSolicitacoesPorDataRelatorioFechamento(page, id, false, inicio, inicio);
			}else {
				return repository.listarSolicitacoesPorDataRelatorioAtualizado(page, id, false, inicio, inicio);
			}
		}
			
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioData(Pageable page, Long id, String periodo, LocalDate ini, LocalDate termino) {
		LocalDateTime inicio, fim;
		
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			
			if(periodo.equals("abertura")) {
				return repository.listarSolicitacoesPorFuncionarioDataAbertura(page, id, false, inicio, fim);
			}else if(periodo.equals("fechamento")) {
				return repository.listarSolicitacoesPorFuncionarioDataFechamento(page, id, false, inicio, fim);
			}else {
				return repository.listarSolicitacoesPorFuncionarioDataAtualizado(page, id, false, inicio, fim);
			}
			
		}else if(ini != null) {
			
			inicio = ini.atTime(00, 00, 00);
			fim = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 23, 59, 59);
			if(periodo.equals("abertura")) {
				return repository.listarSolicitacoesPorFuncionarioDataAbertura(page, id, false, inicio, fim);
			}else if(periodo.equals("fechamento")) {
				return repository.listarSolicitacoesPorFuncionarioDataFechamento(page, id, false, inicio, fim);
			}else {
				return repository.listarSolicitacoesPorFuncionarioDataAtualizado(page, id, false, inicio, fim);
			}
		} else {
			inicio = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
			if(periodo.equals("abertura")) {
				return repository.listarSolicitacoesPorFuncionarioDataAbertura(page, id, false, inicio, inicio);
			}else if(periodo.equals("fechamento")) {
				return repository.listarSolicitacoesPorFuncionarioDataFechamento(page, id, false, inicio, inicio);
			}else {
				return repository.listarSolicitacoesPorFuncionarioDataAtualizado(page, id, false, inicio, inicio);
			}
		}
	}
	
	public DtoRelatorioFuncionario listarRelatoriosPorFuncionarioData(Long id, LocalDate ini, LocalDate termino) {
		LocalDateTime inicio, fim;
		
		int qtdAbertos,qtdFechados,qtdAtualizados;
		
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			qtdAbertos = repository.totalabertasPeriodoPorFuncionario(id, false, inicio, fim);
			qtdFechados = repository.totalFechadasPeriodoPorFuncionario(id, false, inicio, fim);
			qtdAtualizados = repository.totalAtualizadosPeriodoPorFuncionario(id, false, inicio, fim);
			return new DtoRelatorioFuncionario(qtdAbertos, qtdFechados, qtdAtualizados);
		}else if(ini != null) {
			inicio = ini.atTime(00, 00, 00);
			fim = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 23, 59, 59);
			qtdAbertos = repository.totalabertasPeriodoPorFuncionario(id, false, inicio, fim);
			qtdFechados = repository.totalFechadasPeriodoPorFuncionario(id, false, inicio, fim);
			qtdAtualizados = repository.totalAtualizadosPeriodoPorFuncionario(id, false, inicio, fim);
			return new DtoRelatorioFuncionario(qtdAbertos, qtdFechados, qtdAtualizados);
		} else {
			inicio = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
			qtdAbertos = repository.totalabertasPeriodoPorFuncionario(id, false, inicio, inicio);
			qtdFechados = repository.totalFechadasPeriodoPorFuncionario(id, false, inicio, inicio);
			qtdAtualizados = repository.totalAtualizadosPeriodoPorFuncionario(id, false, inicio, inicio);
			return new DtoRelatorioFuncionario(qtdAbertos, qtdFechados, qtdAtualizados);
		}
	} 
	
//	public Page<SolicitacaoProjecao> listarSolicitacoesAtualizadasHoje(Pageable page) {
//		LocalDateTime hoje = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
//		return repository.listarSolicitacoesAtualizadasHoje(page, hoje);
//	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesRelatorioHoje(Pageable page, String status) {
		LocalDateTime hojeInicio = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
		LocalDateTime hojeFim = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 23, 59, 00);
		
		if(status.equals("atualizadas")) {
			return repository.listarSolicitacoesAtualizadasHoje(page, hojeInicio, hojeFim);
		} else if(status.equals("finalizadas")) {
			return repository.listarSolicitacoesFinalizadasHoje(page, hojeInicio, hojeFim);
		}else if(status.equals("abertas")) {
			return repository.listarSolicitacoesAbertasHoje(page, hojeInicio, hojeFim);
		}else if(status.equals("agendadas")) {
			return repository.listarSolicitacoesAgendadasHoje(page, hojeInicio, hojeFim);
		}else if(status.equals("atrasadas")) {
			return repository.listarSolicitacoesAgendadasAtrasadas(page, LocalDateTime.now());
		}else {
			return null;
		}
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadasHoje(Pageable page) {
		LocalDateTime hojeInicio = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
		LocalDateTime hojeFim = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 23, 59, 00);
		return repository.listarSolicitacoesFinalizadasHoje(page, hojeInicio, hojeFim);
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesAbertasHoje(Pageable page) {
		LocalDateTime hojeInicio = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
		LocalDateTime hojeFim = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 23, 59, 00);
		return repository.listarSolicitacoesAbertasHoje(page, hojeInicio, hojeFim);
	}
	
	
//	public Page<SolicitacaoProjecao> listarSolicitacoesPorData(Pageable page, DtoDadosRelatorioCsv dto) {
//		
//		LocalDateTime inicio, fim;
//		
//		if(dto.inicio() != null  && dto.fim() != null ) {
//			inicio = LocalDateTime.of(dto.inicio().getYear(), dto.inicio().getMonth(), dto.inicio().getDayOfMonth(), 00, 00, 00);
//			fim = LocalDateTime.of(dto.fim().getYear(), dto.fim().getMonth(), dto.fim().getDayOfMonth(), 00, 00, 00);
//			return repository.listarSolicitacoesPorDataRelatorio(page, dto.cliente_id(), false, inicio, fim);
//			
//		}else if(dto.inicio() != null) {
//			inicio = LocalDateTime.of(dto.inicio().getYear(), dto.inicio().getMonth(), dto.inicio().getDayOfMonth(), 00, 00, 00);
//			fim = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
//			return repository.listarSolicitacoesPorDataRelatorio(page, dto.cliente_id(), false, inicio, fim);
//		} else {
//			inicio = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 00, 00, 00);
//			return repository.listarSolicitacoesPorDataRelatorio(page, dto.cliente_id(), false, inicio, inicio);
//		}
//		
//			
//	}
	
	
	public DtoSolicitacaoProjecaoCompleta buscarSolicitacaoRelatorio(Long id) {
		return new DtoSolicitacaoProjecaoCompleta(repository.buscarSolicitacaoRelatorio(id));
	}



	

}
