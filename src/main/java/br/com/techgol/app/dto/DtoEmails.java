package br.com.techgol.app.dto;

import br.com.techgol.app.model.enums.Agendamentos;

public record DtoEmails(
		Long id,
		Agendamentos agendamento,
		String email,
		boolean status
		) {

}
