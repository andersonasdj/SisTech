package br.com.techgol.app.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface DtoLogin {
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getdataLogin();

}
