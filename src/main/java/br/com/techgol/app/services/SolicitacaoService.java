package br.com.techgol.app.services;

import java.time.Duration;
import java.time.LocalDateTime;
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
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
import br.com.techgol.app.dto.DtoSolicitacaoProjecaoCompleta;
import br.com.techgol.app.dto.DtoSolicitacaoRelatorios;
import br.com.techgol.app.dto.DtoSolicitacoesRelatorioFuncionario;
import br.com.techgol.app.dto.dashboard.DtoDashboard;
import br.com.techgol.app.dto.dashboard.DtoDashboardResumoFuncionario;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.repository.SolicitacaoRepository;
import jakarta.transaction.Transactional;

@Service
public class SolicitacaoService {
	
	@Autowired
	private SolicitacaoRepository repository;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Transactional
	public String exclusaoLogigaSolicitacao(Long id) {
		
		if(repository.existsById(id)) {
			Solicitacao solicitacao = repository.getReferenceById(id);
			solicitacao.setStatus(Status.EXCLUIDO);
			solicitacao.setExcluido(true);
			solicitacao.setDataAgendado(null);
			solicitacao.setDataAndamento(null);
			return "Excluido com sucesso!";
		}else {
			return "Não foi possível excluir";
		}
	}
	
	public Solicitacao edicaoRapida(DtoDadosEdicaoRapida dados) {
		
		Solicitacao solicitacao = repository.getReferenceById(dados.id());
		
		if(dados.nomeFuncionario() != null && dados.nomeFuncionario() != "") {
			if(funcionarioService.existePorNomeFuncionario(dados.nomeFuncionario())) {
				Funcionario funcionario = funcionarioService.buscaPorNome(dados.nomeFuncionario());
				solicitacao.setFuncionario(funcionario);
			}
		}
		if(dados.status().equals(Status.ANDAMENTO)) {
			solicitacao.setDataAndamento(LocalDateTime.now());
			solicitacao.setDataAgendado(null);
		}
		if(dados.status().equals(Status.ABERTO) || dados.status().equals(Status.AGENDADO)) {
			solicitacao.setDataAndamento(null);
			
			
			if(dados.dataAgendado() != null) {
				if(!dados.dataAgendado().isBlank() || !dados.dataAgendado().isEmpty()) {
					solicitacao.setDataAgendado(LocalDateTime.parse(dados.dataAgendado()+"T"+dados.horaAgendado()));
				}
			}
			
		}
		if(dados.status().equals(Status.AGUARDANDO) || dados.status().equals(Status.PAUSADO)) {
			
			if(solicitacao.getDataAndamento() != null) {
				solicitacao.setDuracao(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes());
			}
			solicitacao.setDataAndamento(null);
			solicitacao.setDataAgendado(null);
			
		}
		if(dados.status().equals(Status.FINALIZADO)) {
			solicitacao.setDataFinalizado(LocalDateTime.now());
			if(solicitacao.getDuracao() == null) {
				if(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes() < 15) {
					solicitacao.setDuracao(15l);
				}else {
					solicitacao.setDuracao(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes());
				}
			}else {
				Long tempoAnterior = solicitacao.getDuracao();
				solicitacao.setDuracao(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes() + tempoAnterior);
			}
			
		}
		if(solicitacao.getStatus().equals(Status.EXCLUIDO) && dados.status().equals(Status.ABERTO)) {
			solicitacao.setExcluido(false);
			solicitacao.setDataAndamento(null);
		}
		solicitacao.setStatus(dados.status());
		solicitacao.setDescricao(dados.descricao());
		solicitacao.setResolucao(dados.resolucao());
		solicitacao.setObservacao(dados.observacao());
		solicitacao.setCategoria(dados.categoria());
		solicitacao.setClassificacao(dados.classificacao());
		solicitacao.setPrioridade(dados.prioridade());
		solicitacao.setLocal(dados.local());
		return repository.save(solicitacao);
	}

	public List<Solicitacao> buscarTodos() {
		return repository.findAll();
	}

	public Page<SolicitacaoProjecao> listarSolicitacoes(Pageable page, String status, Boolean exluida) {
		return repository.listarSolicitacoes(page, status, exluida);
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadas(Pageable page, String status, Boolean exluida) {
		return repository.listarSolicitacoesFinalizadas(page, status, exluida);
	}
	
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadasPorCliente(Pageable page, Long id) {
		return repository.listarSolicitacoesFinalizadasPorCliente(page, id);
	}

	public Solicitacao buscarPorId(Long id) {
		return repository.getReferenceById(id);
	}

	public DtoSolicitacaoComFuncionario salvarNovaSolicitacao(Solicitacao solicitacao) {
		Funcionario funcionarioBase = funcionarioService.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
		solicitacao.setAbertoPor(funcionarioBase.getNomeFuncionario());
		if(solicitacao.getStatus() == Status.ANDAMENTO) {
			solicitacao.setDataAndamento(LocalDateTime.now());
		}
		return new DtoSolicitacaoComFuncionario(repository.save(solicitacao));
		
	}
	
	public DtoDashboardCliente geraDashboardCliente(Long id) {
		
		int onsite,offsite,problema,incidente,solicitacao,backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,pausado,finalizado,totalSolicitacoes;
		
		onsite = repository.totalPorLocalPorCliente(id, Local.ONSITE.toString() , false);
		offsite = repository.totalPorLocalPorCliente(id, Local.OFFSITE.toString() , false);
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

		return new DtoDashboardCliente(onsite,offsite,problema,incidente,solicitacao,backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,pausado,finalizado,totalSolicitacoes);
		
	}
	

	public DtoDashboard geraDashboard() {
		
		int onsite,offsite,problema,incidente,solicitacao,backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,pausado,totalSolicitacoes;
		
		onsite = repository.totalPorLocal(Local.ONSITE.toString() , false, Status.FINALIZADO.toString());
		offsite = repository.totalPorLocal(Local.OFFSITE.toString() , false, Status.FINALIZADO.toString());
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
		
		
		return new DtoDashboard(onsite,offsite,problema,incidente,solicitacao,backup,acesso,evento,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,pausado,totalSolicitacoes,totalFUncionarios,listaDto);
	}

	public DtoSolicitacaoRelatorios geraRelatorios() {
		
		//Long abertosHoje, finalizadosHoje;
		List<Long> idFuncionarios = funcionarioService.listarIdFuncionarosLong();
		List<DtoSolicitacoesRelatorioFuncionario> relatorioFuncionario = new ArrayList<>();

		idFuncionarios.forEach(f -> {
			Long abertos, andamento, agendados, aguardando, total;
			abertos = repository.countByStatusAndFuncionarioId(Status.ABERTO, f);
			andamento = repository.countByStatusAndFuncionarioId(Status.ANDAMENTO, f);
			agendados = repository.countByStatusAndFuncionarioId(Status.AGENDADO, f);
			aguardando = repository.countByStatusAndFuncionarioId(Status.AGUARDANDO, f);
			total = abertos+andamento+agendados+aguardando;
			relatorioFuncionario.add(new DtoSolicitacoesRelatorioFuncionario(funcionarioService.buscaNomeFuncionarioPorId(f), abertos, andamento, agendados, aguardando, total));
			
		});
		
		return new DtoSolicitacaoRelatorios(10l,2l, relatorioFuncionario);
	}

	@Transactional
	public DtoDadosRestauracao restaurar(Long id) {
		
		Solicitacao solicitacao = repository.getReferenceById(id);
		solicitacao.setExcluido(false);
		solicitacao.setStatus(Status.ABERTO);
		solicitacao.setDataAndamento(null);
		solicitacao.setDataAgendado(null);
		return new DtoDadosRestauracao(solicitacao.getId());
				
	}

	public DtoSolicitacaoProjecaoCompleta buscarFinalizada(Long id) {
		return new DtoSolicitacaoProjecaoCompleta(repository.buscarSolicitacaoFinalizada(id, Status.FINALIZADO.toString()));
	} 

}
