package br.com.techgol.app.dto;

import java.time.LocalDateTime;

import br.com.techgol.app.model.LogLogin;

public record DtoLogAcesso(
		Long id,
		LocalDateTime dataLogin,
		String enderecoServer,
		String hostname,
		String ip,
		String nomeLocal,
		String pais,
		String uri,
		String usuario
		
		) {
	
	
	public DtoLogAcesso(LogLogin l) {
		this(
			l.getId(),
			l.getDataLogin(),
			l.getEnderecoServer(),
			l.getHostname(),
			l.getIp(),
			l.getNomeLocal(),
			l.getPais(),
			l.getUri(),
			l.getUsuario()
				);
		
		
	}

}
