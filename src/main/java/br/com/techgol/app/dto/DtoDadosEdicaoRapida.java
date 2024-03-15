package br.com.techgol.app.dto;

import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoDadosEdicaoRapida(
		
		Long id,
		String descricao,
		String resolucao,
		String observacao,
		Status status,
		Categoria categoria,
		Classificacao classificacao,
		Prioridade prioridade,
		Local local,
		String nomeFuncionario,
		String dataAgendado,
		String horaAgendado) {

//	public DtoDadosEdicaoRapida(Solicitacao dados) {
//		this(
//				dados.getId(),
//				dados.getDescricao(),
//				dados.getResolucao(),
//				dados.getObservacao(),
//				dados.getStatus(),
//				dados.getCategoria(),
//				dados.getClassificacao(),
//				dados.getPrioridade(),
//				dados.getLocal(),
//				dados.getFuncionario().getNomeFuncionario());
//	}

}
