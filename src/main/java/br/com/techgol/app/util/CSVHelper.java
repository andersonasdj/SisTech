package br.com.techgol.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import br.com.techgol.app.orm.SolicitacaoProjecao;

public class CSVHelper {
	
	public static ByteArrayInputStream solicitacoesNaoFinalizadasToCSV(List<SolicitacaoProjecao> solicitacoes) {
	    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withHeader("ID", "DataAbertura", "AbertoPor", "Cliente", 
	    		"Solicitante", "Afetado", "Descricao", "Categoria", "FormaAbertura", "Local", "Observacao", "Prioridade", "Resolucao", "Status",
	    		"Funcionario", "DataAtualizacao", "Duracao(minutos)", "Versao");

	    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
	        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
	      for (SolicitacaoProjecao solicitacao : solicitacoes) {
	        List<String> data = Arrays.asList(
	              String.valueOf(solicitacao.getId()),
	              solicitacao.getDataAbertura().toString(),
	              solicitacao.getAbertoPor(),
	              solicitacao.getNomeCliente(),
	              solicitacao.getSolicitante(),
	              solicitacao.getAfetado(),
	              solicitacao.getDescricao(),
	              solicitacao.getCategoria(),
	              solicitacao.getFormaAbertura(),
	              solicitacao.getLocal(),
	              solicitacao.getObservacao(),
	              solicitacao.getPrioridade(),
	              solicitacao.getResolucao(),
	              solicitacao.getStatus(),
	              solicitacao.getNomeFuncionario(),
	              solicitacao.getDataAtualizacao().toString(),
	              (solicitacao.getDuracao()) != null ? solicitacao.getDuracao().toString() : "",
	              solicitacao.getVersao()
	            );

	        csvPrinter.printRecord(data);
	      }

	      csvPrinter.flush();
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
	    }
	  }
}
