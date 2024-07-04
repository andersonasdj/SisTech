package br.com.techgol.app.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.TimeSheet;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.repository.TimesheetRepository;
import jakarta.transaction.Transactional;

@Service
public class TimeSheetService {
	
	@Autowired
	private TimesheetRepository repository;
	
	public TimeSheet cadastraTimesheet(Solicitacao solicitacao, Funcionario funcionario, LocalDateTime inicio, LocalDateTime fim, Long duracao, Status status) {
		TimeSheet timeSheet = new TimeSheet(solicitacao, funcionario, inicio, fim, duracao, status);
		return repository.save(timeSheet);
	}

	@Transactional
	public void atualizaTimesheet(Solicitacao solicitacao) {
		TimeSheet timeSheet =  repository.buscaTimeSheetParaEdicao(solicitacao.getId(), solicitacao.getFuncionario().getId(), solicitacao.getStatus().toString());
		if(timeSheet != null) {
			timeSheet.setInicio(solicitacao.getDataAndamento());
			timeSheet.setFim(solicitacao.getDataFinalizado());
			timeSheet.setDuracao(solicitacao.getDuracao());
		}
	}
	
	public Long minutosPorFuncionarioPeriodo(Long id, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		
		if(ini != null  && termino != null ) {
			System.out.println("SERVICE NAO NULL");
			inicio = ini.atTime(00, 00, 00);
			fim = termino.atTime(23, 59, 59);
			System.out.println("INICIO: " + inicio);
			System.out.println("FIM: " + fim);
			return repository.buscarMinutosPorFuncionarioPeriodo(id, inicio, fim);
		}else {
			System.out.println("SERVICE NULL");
			inicio = LocalDateTime.now().withNano(0);
			fim = LocalDateTime.now().withNano(0);
			return repository.buscarMinutosPorFuncionarioPeriodo(id, inicio, fim);
		}
		
	}
}
