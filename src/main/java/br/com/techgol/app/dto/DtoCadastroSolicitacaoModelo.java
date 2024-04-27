package br.com.techgol.app.dto;

import br.com.techgol.app.model.enums.Status;
import jakarta.validation.constraints.NotBlank;

public record DtoCadastroSolicitacaoModelo(
		String solicitante,
		String afetado,
		Status status,
		@NotBlank
		Long nomeCliente,
		Long nomeFuncionario,
		Long modelo
		
		) {

}
