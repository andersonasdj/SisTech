package br.com.techgol.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgol.app.model.ClienteInfraestrutura;

public interface InfraClienteRepository extends JpaRepository<ClienteInfraestrutura, Long>{

	Optional<ClienteInfraestrutura> findByClienteId(Long clienteId);
}
