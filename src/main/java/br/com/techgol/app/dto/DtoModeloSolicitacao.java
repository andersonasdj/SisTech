package br.com.techgol.app.dto;

import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoModeloSolicitacao(
		Long idConjunto,
		FormaAbertura formaAbertura,
		String descricao,
		String observacao,
		Categoria categoria,
		Classificacao classificacao,
		Prioridade prioridade,
		Local local,
		Status status
		) {

}
