package br.com.techgol.app.dto;

import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoSolicitacaoModelo(
		
		Long id,
		Categoria categoria,
		Classificacao classificacao,
		String descricao,
		FormaAbertura formaAbertura,
		Local local,
		String observacao,
		Prioridade prioridade,
		Status status
		) {

}
