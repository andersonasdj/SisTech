package br.com.techgol.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgol.app.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
