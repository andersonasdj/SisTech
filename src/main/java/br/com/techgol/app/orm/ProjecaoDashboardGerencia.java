package br.com.techgol.app.orm;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ProjecaoDashboardGerencia {
	
	Long getId();
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate getDataAbertura();

}
