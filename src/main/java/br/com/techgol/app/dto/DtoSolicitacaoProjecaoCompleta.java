package br.com.techgol.app.dto;

import java.time.format.DateTimeFormatter;

import br.com.techgol.app.orm.SolicitacaoProjecaoCompleta;

public record DtoSolicitacaoProjecaoCompleta(
		Long id,
		String abertoPor,
		String afetado,
		String categoria,
		String classificacao,
		String descricao,
		String formaAbertura,
		String local,
		String observacao,
		String prioridade,
		String resolucao,
		String solicitante,
		String status,
		String duracao,
		String nomeCliente,
		String nomeFuncionario,
		String dataAbertura,
		String horaAbertura,
		String dataAndamento,
		String horaAndamento,
		String dataFinalizado,
		String horaFinalizado,
		String dataAtualizacao
		
		) {

	public DtoSolicitacaoProjecaoCompleta(SolicitacaoProjecaoCompleta s) {
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
				s.getNomeCliente(),
				s.getNomeFuncionario(),
				s.getDataAbertura().toLocalDate().toString(),
				s.getDataAbertura().format(DateTimeFormatter.ofPattern("HH:mm")),
				(s.getDataAndamento()) != null? s.getDataAndamento().toLocalDate().toString() : " ",
				(s.getDataAndamento()) != null? s.getDataAndamento().format(DateTimeFormatter.ofPattern("HH:mm")) : " ",
				(s.getDataFinalizado()) != null? s.getDataFinalizado().toLocalDate().toString() : "",
				(s.getDataFinalizado()) != null? s.getDataFinalizado().format(DateTimeFormatter.ofPattern("HH:mm")): " ",
				s.getDataAtualizacao().toString()
				);
	}

}
