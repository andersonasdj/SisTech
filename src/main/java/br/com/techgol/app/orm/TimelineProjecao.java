package br.com.techgol.app.orm;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface TimelineProjecao {
	
	Long getId();
	
	String getNomeCliente();
	
	Long getDuracao();
	
	String getStatus();
	
	Long getSolicitacao_id();
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getInicio();
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getFim();

}
