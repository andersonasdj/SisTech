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
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
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
		if(dados.status().equals(Status.AGUARDANDO)) {
			
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

	public DtoDashboard geraDashboard() {
		
		int onsite,offsite,problema,incidente,solicitacao,backup,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,totalSolicitacoes;
		
		onsite = repository.countByLocalAndExcluido(Local.ONSITE, false);
		offsite = repository.countByLocalAndExcluido(Local.OFFSITE, false);
		problema = repository.countByClassificacaoAndExcluido(Classificacao.PROBLEMA, false);
		incidente = repository.countByClassificacaoAndExcluido(Classificacao.INCIDENTE, false);
		solicitacao = repository.countByClassificacaoAndExcluido(Classificacao.SOLICITACAO, false);
		backup = repository.countByClassificacaoAndExcluido(Classificacao.BACKUP, false);
		baixa = repository.countByPrioridadeAndExcluido(Prioridade.BAIXA, false);
		media = repository.countByPrioridadeAndExcluido(Prioridade.MEDIA, false);
		alta = repository.countByPrioridadeAndExcluido(Prioridade.ALTA, false);
		critica = repository.countByPrioridadeAndExcluido(Prioridade.CRITICA, false);
		planejada = repository.countByPrioridadeAndExcluido(Prioridade.PLANEJADA, false);
		aberto = repository.countByStatusAndExcluido(Status.ABERTO, false);
		andamento = repository.countByStatusAndExcluido(Status.ANDAMENTO, false);
		agendado = repository.countByStatusAndExcluido(Status.AGENDADO, false);
		aguardando = repository.countByStatusAndExcluido(Status.AGUARDANDO, false);
		totalSolicitacoes = aberto+andamento+agendado+aguardando;
		
		List<DtoListarFuncionarios> funcionarios = funcionarioService.listar();
		List<DtoDashboardResumoFuncionario> listaDto = new ArrayList<>();
		
		int totalFUncionarios = funcionarios.size();
		
		funcionarios.forEach(f -> {
			listaDto.add(new DtoDashboardResumoFuncionario(f.nomeFuncionario(),repository.countByFuncionarioIdAndExcluido(f.id(), false)));
		});
		
		
		return new DtoDashboard(onsite,offsite,problema,incidente,solicitacao,backup,baixa,media,alta,critica,planejada,aberto,andamento,agendado,aguardando,totalSolicitacoes,totalFUncionarios,listaDto);
	}

	public DtoSolicitacaoRelatorios geraRelatorios() {
		
		Long abertosHoje, finalizadosHoje;
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

}
