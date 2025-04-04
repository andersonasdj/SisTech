package br.com.techgol.app.orm;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface TimesheetProjecao {
	
	Long getId();
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getInicio();
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getFim();

	String getStatus();

	Long getDuracao();
	
	String getNomeFuncionario();
}
