package br.com.techgol.app.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.CustoOperacionalProjection;
import br.com.techgol.app.dto.MetricasClienteProjection;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.TimeSheet;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.TimelineProjecao;
import br.com.techgol.app.orm.TimesheetProjecao;
import br.com.techgol.app.repository.SolicitacaoRepository;
import br.com.techgol.app.repository.TimesheetRepository;
import br.com.techgol.app.util.TimeSheetUtils;
import jakarta.transaction.Transactional;

@Service
public class TimeSheetService {
	
	@Autowired private TimesheetRepository repository;
	@Autowired private SolicitacaoRepository solicitacaoRepository;
	
	public Long timesheetPorFuncionarioPeriodoMinutos(Long id, LocalDateTime inicio, LocalDateTime fim) {
	    
	    List<TimeSheet> timeSheets = repository.buscarTimesheetPorFuncionarioPeriodo(id, inicio, fim);
	    
	    List<TimeSheet> registrosNoPeriodo = timeSheets.stream()
	            .filter(ts -> !ts.getInicio().isBefore(inicio) && !ts.getFim().isAfter(fim))
	            .collect(Collectors.toList());

	    // Calcula duração total sem sobreposição
	    Duration total = TimeSheetUtils.calcularTempoTotalPorDiaSemSobreposicao(registrosNoPeriodo);

	    return total.toMinutes();        
	}
	
	public List<TimeSheet> timesheetPorFuncionarioPeriodo(Long id, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			return repository.buscarTimesheetPorFuncionarioPeriodo(id, inicio, fim);
		}else {
			inicio = LocalDateTime.now().withNano(0);
			fim = LocalDateTime.now().withNano(0);
			return repository.buscarTimesheetPorFuncionarioPeriodo(id, inicio, fim);
		}
		
	}
	
	public TimeSheet cadastraTimesheet(Solicitacao solicitacao, Funcionario funcionario, LocalDateTime inicio, LocalDateTime fim, Long duracao, Status status) {
		TimeSheet timeSheet = new TimeSheet(solicitacao, funcionario, inicio, fim, duracao, status);
		return repository.save(timeSheet);
	}

//	@Transactional
//	public void atualizaTimesheet(Solicitacao solicitacao) {
//		
//		TimeSheet timeSheet =  repository.buscaTimeSheetParaEdicao(solicitacao.getId(), solicitacao.getFuncionario().getId(), solicitacao.getStatus().toString());
//		
//		if(timeSheet != null) {
//			timeSheet.setInicio(solicitacao.getDataAndamento());
//			timeSheet.setFim(solicitacao.getDataFinalizado());
//			timeSheet.setDuracao(solicitacao.getDuracao());
//		}
//	}
	
	public void atualizaTimesheet(Solicitacao solicitacao) {
		
		repository.deleteBySolicitacao_idAndFuncionario_id(solicitacao.getId(), solicitacao.getFuncionario().getId());
		TimeSheet timeSheet =  new TimeSheet(solicitacao);
		repository.save(timeSheet);
		
	}
	
	
	public Long minutosPorFuncionarioPeriodo(Long id, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			return repository.buscarMinutosPorFuncionarioPeriodo(id, inicio, fim);
		}else {
			inicio = LocalDateTime.now().withNano(0);
			fim = LocalDateTime.now().withNano(0);
			return repository.buscarMinutosPorFuncionarioPeriodo(id, inicio, fim);
		}
		
	}

	public List<TimelineProjecao> timelinePorFuncionarioPeriodo(Long id, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		
		if(ini != null  && termino != null ) {
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			return repository.listarTimeline(id, inicio, fim);
		}else {
			inicio = LocalDateTime.now().withNano(0);
			fim = LocalDateTime.now().withNano(0);
			return repository.listarTimeline(id, inicio, fim);
		}
	}

	public void deletaSolicitacao(Long id) {
		repository.deleteBySolicitacaoId(id);
		
	}

	public boolean findById(Long id) {
		return repository.existsBySolicitacaoId(id);
	}

	public Page<TimesheetProjecao> listaTimesheetPorIdSolicitacao(Pageable page,  Long id) {
		
		return repository.listarTimesheetProjecao(page, id);
		
	}
	
	@Transactional
	public void deletaTimesheetPorId(Long id) {
		Long idSolicitacao = repository.buscaPorId(id);
		repository.deleteById(id);
		Long duracao = repository.bucarDuracaoIdSolicitacao(idSolicitacao);
		
		if(solicitacaoRepository.existsById(idSolicitacao)) {
			Solicitacao solicitacao = solicitacaoRepository.getReferenceById(idSolicitacao);
			if(duracao != null) {
				solicitacao.setDuracao(duracao);
			}else {
				solicitacao.setDuracao(0l);
			}
		}else {
			System.out.println("Não foi possivel recalcular a duracao da solicitacao");
		}
	}

	public Long totalHorasPeriodoPorFuncionario(Long id, LocalDateTime inicio, LocalDateTime fim) {
		return repository.totalHorasPeriodoPorFuncionario(id, inicio, fim);
	}

	public BigDecimal custoOperacionalTecPorCliente(Long id, LocalDateTime inicio, LocalDateTime fim, Long idFuncionario) {
		return repository.custoOperacionalTecPorCliente(id, inicio, fim, idFuncionario);
	}

	public List<CustoOperacionalProjection> buscarCustosAgrupados(LocalDateTime inicio, LocalDateTime fim) {
		return repository.buscarCustosAgrupados(inicio, fim);
	}

	public List<MetricasClienteProjection> buscarMetricasPorCliente(LocalDateTime inicio, LocalDateTime fim) {
		return solicitacaoRepository.buscarMetricasPorCliente(inicio, fim);
	}
	
}
