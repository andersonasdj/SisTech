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

	public ByteArrayInputStream loadRelatorioPorClienteFiltro(Long id, String periodo, LocalDate ini, LocalDate termino,
			String abertura, String categoria, String classificacao, String local, String prioridade, String nomeFuncionario) {
		
		if(abertura.equals("*")) {abertura = "";}
		if(categoria.equals("*")) {categoria = "";}
		if(classificacao.equals("*")) {classificacao = "";}
		if(local.equals("*")) {local = "";}
		if(prioridade.equals("*")) {prioridade = "";}
		if(nomeFuncionario.equals("*")) {nomeFuncionario = "";}
		if (id != null && id == -1L) { 
	        // Ou qualquer valor que esteja usando para representar "não selecionado"
	        id = null;
	    }
		return buscaSolicitacoesFiltradasParaCsv(id, periodo, ini, termino, abertura, categoria, classificacao, local, prioridade, nomeFuncionario);
	}
	
	public ByteArrayInputStream loadRelatorioPorCliente(Long id, String periodo, LocalDate ini, LocalDate termino) {
		
		String abertura = "" , categoria = "", classificacao = "", local = "", prioridade = "", nomeFuncionario = "";
		return buscaSolicitacoesFiltradasParaCsv(id, periodo, ini, termino, abertura, categoria, classificacao, local, prioridade, nomeFuncionario);
	}

	private ByteArrayInputStream buscaSolicitacoesFiltradasParaCsv(Long id, String periodo, LocalDate ini,
			LocalDate termino, String abertura, String categoria, String classificacao, String local, String prioridade,
			String nomeFuncionario) {
		LocalDateTime inicio, fim;
		inicio = ini.atTime(00, 00, 00);
		fim = termino.atTime(23, 59, 59);
		
		List<SolicitacaoProjecao> solicitacao;
		if(periodo.equals("abertura")) {
			solicitacao = repository.listarSolicitacoesPorPeriodoAberturaDataCsv(id, false, inicio, fim, abertura, categoria, classificacao, local, prioridade);
		}else if(periodo.equals("fechamento")) {
			solicitacao = repository.listarSolicitacoesPorPeriodoFechamentoDataCsv(id, false, inicio, fim, abertura, categoria, classificacao, local, prioridade);
		}else {
			solicitacao = repository.listarSolicitacoesPorPeriodoAtualizadoDataCsv(id, false, inicio, fim, abertura, categoria, classificacao, local, prioridade);
		}
		
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
