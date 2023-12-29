package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	@Query(value = "SELECT c.nomeCliente FROM clientes c ORDER BY c.nomeCliente", nativeQuery = true)
	public List<String> listarNomesCliente();
	
	
	@Query(value = "SELECT c.id FROM clientes c ORDER BY c.nomeCliente", nativeQuery = true)
	public List<String> listarIdCliente();
	
	
	public Cliente findBynomeCliente(String nomeCliente);
	
}
