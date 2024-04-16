package br.com.techgol.app.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoSolicitacaoProjecaoCompletaColaboradores(
		Long id,
		String abertoPor,
		String afetado,
		Categoria categoria,
		Classificacao classificacao,
		String descricao,
		FormaAbertura formaAbertura,
		Local local,
		String observacao,
		Prioridade prioridade,
		String resolucao,
		String solicitante,
		Status status,
		Long duracao,
		String nomeCliente,
		String nomeFuncionario,
		String dataAbertura,
		String horaAbertura,
		String dataAndamento,
		String horaAndamento,
		String dataFinalizado,
		String horaFinalizado,
		String dataAtualizacao,
		List<String> funcionarios,
		List<String> colaboradores
		
		) {

	public DtoSolicitacaoProjecaoCompletaColaboradores(Solicitacao s, List<String> f, List<String> c) {
		this(
				s.getId(),
				s.getAbertoPor(),
				s.getAfetado(),
				s.getCategoria(),
				s.getClassificacao(),
				s.getDescricao(),
				s.getFormaAbertura(),
				s.getLocal(),
				s.getObservacao(),
				s.getPrioridade(),
				s.getResolucao(),
				s.getSolicitante(),
				s.getStatus(),
				s.getDuracao(),
				s.getCliente().getNomeCliente(),
				s.getFuncionario().getNomeFuncionario(),
				s.getDataAbertura().toLocalDate().toString(),
				s.getDataAbertura().format(DateTimeFormatter.ofPattern("HH:mm")),
				(s.getDataAndamento()) != null? s.getDataAndamento().toLocalDate().toString() : " ",
				(s.getDataAndamento()) != null? s.getDataAndamento().format(DateTimeFormatter.ofPattern("HH:mm")) : " ",
				(s.getDataFinalizado()) != null? s.getDataFinalizado().toLocalDate().toString() : "",
				(s.getDataFinalizado()) != null? s.getDataFinalizado().format(DateTimeFormatter.ofPattern("HH:mm")): " ",
				s.getDataAtualizacao().toString(),
				f,
				c
				);
	}

}
