package br.com.techgol.app.dto;

import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoSolicitacaoFinalizada(
		
		Long id,
		String abertoPor,
		String afetado,
		Categoria categoria,
		String dataAbertura,
		String horaAbertura,
		String dataAndamento,
		String horaAndamento,
		String dataFinalizado,
		String horaFinalizado,
		String descricao,
		Long duracao,
		FormaAbertura formaAbertura,
		Local local,
		String observacao, 
		Prioridade prioridade,
		String resolucao,
		String solicitante,
		String nomeFuncionario,
		Status status,
		Classificacao classificacao
		) {

}
