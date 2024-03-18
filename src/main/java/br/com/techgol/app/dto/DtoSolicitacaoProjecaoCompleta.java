package br.com.techgol.app.dto;

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
		String dataAbertura
		
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
				s.getDataAbertura()
				);
	}

}
