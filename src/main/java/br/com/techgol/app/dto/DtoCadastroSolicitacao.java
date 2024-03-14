package br.com.techgol.app.dto;

import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;
import jakarta.validation.constraints.NotBlank;

public record DtoCadastroSolicitacao(
		String solicitante,
		String afetado,
		String descricao,
		String observacao,
		String abertoPor,
		Prioridade prioridade,
		FormaAbertura formaAbertura,
		Categoria categoria,
		Classificacao classificacao,
		Local local,
		Status status,
		@NotBlank
		Long nomeCliente,
		Long nomeFuncionario,
		String dataAgendado,
		String horaAgendado
		
		) {
		

}
