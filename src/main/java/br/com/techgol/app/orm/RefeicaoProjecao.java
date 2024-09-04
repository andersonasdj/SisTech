package br.com.techgol.app.orm;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface RefeicaoProjecao {
	
	Long getId();
	Long getDuracao();
	String getNomeFuncionario();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getInicio();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getFim();
}
