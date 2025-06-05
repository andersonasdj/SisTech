package br.com.techgol.app.dto;

import br.com.techgol.app.model.Evento;

public record EventoDTOList(
		Long id,
	    String titulo,
	    String dataInicio,
	    String dataFim,
	    String calendarId
	    ) {

	public EventoDTOList(Evento e) {
		this(e.getId(), e.getTitulo(), e.getDataInicio().toString(), e.getDataFim().toString(), e.getCalendarId().toString());
	}

}
