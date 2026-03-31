package br.com.techgol.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgol.app.model.EmailProcessado;

public interface EmailProcessadoRepository
extends JpaRepository<EmailProcessado, String> {
}
