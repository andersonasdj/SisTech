package br.com.techgol.app.dto;

import br.com.techgol.app.model.Avisos;

public record DtoAvisoListar(
		Long id,
		String aviso) {

	public DtoAvisoListar(Avisos aviso) {
		this(aviso.getId(), aviso.getAviso());
	
	}

}
