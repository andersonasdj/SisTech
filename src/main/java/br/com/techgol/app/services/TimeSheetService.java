package br.com.techgol.app.services;

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

}
