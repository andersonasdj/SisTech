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
			solicitacao.setExcluido(true);
			return "Excluido com sucesso!";
		}else {
			return "Não foi possível excluir";
		}
	}
	
	public Solicitacao edicaoRapida(DtoDadosEdicaoRapida dados) {
		
		Solicitacao solicitacao = repository.getReferenceById(dados.id());
		
		System.out.println("#############" + dados.status());
		if(dados.nomeFuncionario() != null && dados.nomeFuncionario() != "") {
			if(funcionarioService.existePorNomeFuncionario(dados.nomeFuncionario())) {
				Funcionario funcionario = funcionarioService.buscaPorNome(dados.nomeFuncionario());
				solicitacao.setFuncionario(funcionario);
			}
		}
		if(dados.status().equals(Status.ANDAMENTO)) {
			solicitacao.setDataAndamento(LocalDateTime.now());
		}
		if(dados.status().equals(Status.ABERTO) || dados.status().equals(Status.AGENDADO)) {
			solicitacao.setDataAndamento(null);
		}
		if(dados.status().equals(Status.FINALIZADO)) {
			solicitacao.setDataFinalizado(LocalDateTime.now());
			if(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes() < 15) {
				solicitacao.setDuracao(15l);
			}else {
				solicitacao.setDuracao(Duration.between(solicitacao.getDataAndamento(), LocalDateTime.now()).toMinutes());
			}
			
		}
		System.out.println(solicitacao.getStatus());
		solicitacao.setStatus(dados.status());
		solicitacao.setDescricao(dados.descricao());
		solicitacao.setResolucao(dados.resolucao());
		solicitacao.setObservacao(dados.observacao());
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
		
		onsite = repository.countByLocal(Local.ONSITE);
		offsite = repository.countByLocal(Local.OFFSITE);
		problema = repository.countByClassificacao(Classificacao.PROBLEMA);
		incidente = repository.countByClassificacao(Classificacao.INCIDENTE);
		solicitacao = repository.countByClassificacao(Classificacao.SOLICITACAO);
		backup = repository.countByClassificacao(Classificacao.BACKUP);
		baixa = repository.countByPrioridade(Prioridade.BAIXA);
		media = repository.countByPrioridade(Prioridade.MEDIA);
		alta = repository.countByPrioridade(Prioridade.ALTA);
		critica = repository.countByPrioridade(Prioridade.CRITICA);
		planejada = repository.countByPrioridade(Prioridade.PLANEJADA);
		aberto = repository.countByStatus(Status.ABERTO);
		andamento = repository.countByStatus(Status.ANDAMENTO);
		agendado = repository.countByStatus(Status.AGENDADO);
		aguardando = repository.countByStatus(Status.AGUARDANDO);
		totalSolicitacoes = aberto+andamento+agendado+aguardando;
		
		List<DtoListarFuncionarios> funcionarios = funcionarioService.listar();
		List<DtoDashboardResumoFuncionario> listaDto = new ArrayList<>();
		
		int totalFUncionarios = funcionarios.size();
		
		funcionarios.forEach(f -> {
			listaDto.add(new DtoDashboardResumoFuncionario(f.nomeFuncionario(),repository.countByFuncionarioId(f.id())));
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

}
