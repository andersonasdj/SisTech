package br.com.techgol.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgol.app.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
