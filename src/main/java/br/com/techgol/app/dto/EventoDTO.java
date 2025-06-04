package br.com.techgol.app.dto;

import br.com.techgol.app.model.Evento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {
    private Long id;
    private String titulo;
    private String dataInicio;
    private String dataFim;
    private Long calendarId;

    public EventoDTO(Evento evento) {
        this.id = evento.getId();
        this.titulo = evento.getTitulo();
        this.dataInicio = evento.getDataInicio().toString(); // ISO-8601
        this.dataFim = evento.getDataFim().toString();
        this.calendarId = evento.getCalendarId();
    }

}
