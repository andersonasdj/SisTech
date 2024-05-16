package br.com.techgol.app.services;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.repository.SolicitacaoRepository;
import br.com.techgol.app.util.CSVHelper;

@Service
public class CSVService {

  @Autowired
  SolicitacaoRepository repository;
  
	public ByteArrayInputStream loadNaoFinalizadas() {
	    List<SolicitacaoProjecao> solicitacao = repository.listarSolicitacoesNaoFinalizadas(Status.FINALIZADO.toString(), false);
	    ByteArrayInputStream in = CSVHelper.solicitacoesNaoFinalizadasToCSV(solicitacao);
	    return in;
	}

	public ByteArrayInputStream loadRelatorioPorCliente(Long id, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		inicio = ini.atTime(00, 00, 00);
		fim = termino.atTime(23, 59, 59);
		List<SolicitacaoProjecao> solicitacao = repository.listarSolicitacoesPorDataCsv(id, false, inicio, fim);
	    ByteArrayInputStream in = CSVHelper.solicitacoesNaoFinalizadasToCSV(solicitacao);
	    return in;
	}
	
	public ByteArrayInputStream loadRelatorioPorFuncionario(Long id, String periodo, LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		inicio = ini.atTime(00, 00, 00);
		fim = termino.atTime(23, 59, 59);
		List<SolicitacaoProjecao> solicitacao;
		if(periodo.equals("abertura")) {
			solicitacao = repository.listarSolicitacoesPorFuncionarioDataAberturaCsv(id, false, inicio, fim);
		}else if(periodo.equals("fechamento")) {
			solicitacao = repository.listarSolicitacoesPorFuncionarioDataFechamentoCsv(id, false, inicio, fim);
		}else {
			solicitacao = repository.listarSolicitacoesPorFuncionarioDataAtualizadoCsv(id, false, inicio, fim);
		}
		
	    ByteArrayInputStream in = CSVHelper.solicitacoesNaoFinalizadasToCSV(solicitacao);
	    return in;
	}
	
public ByteArrayInputStream loadRelatorioPorPeriodo(LocalDate ini, LocalDate termino) {
		
		LocalDateTime inicio, fim;
		inicio = ini.atTime(00, 00, 00);
		fim = termino.atTime(23, 59, 59);
		List<SolicitacaoProjecao> solicitacao = repository.listarSolicitacoesPorPeriodoDataCsv(false, inicio, fim);
	    ByteArrayInputStream in = CSVHelper.solicitacoesNaoFinalizadasToCSV(solicitacao);
	    return in;
	}
}
