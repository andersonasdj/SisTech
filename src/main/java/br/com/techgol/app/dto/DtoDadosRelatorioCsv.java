package br.com.techgol.app.dto;

import java.time.LocalDate;

public record DtoDadosRelatorioCsv(
		Long cliente_id,
		LocalDate inicio,
		LocalDate fim) {

}
