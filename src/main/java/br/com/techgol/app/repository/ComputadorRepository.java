package br.com.techgol.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.techgol.app.model.Computador;

public interface ComputadorRepository extends JpaRepository<Computador, Long> {

	@Query(value = "SELECT * FROM computador c", nativeQuery = true)
	public List<Computador> listarTodos();
	
	@Query(value = "SELECT * FROM computador c WHERE c.cliente_id = :id", nativeQuery = true)
	public List<Computador> listarTodosCliente(Long id);

	
	@Query(nativeQuery = true,
			value = "SELECT c.* "
					+ "FROM computador c "
					+ "INNER JOIN clientes cli ON c.cliente_id=cli.id "
					+ "WHERE c.last_seen <= :limite "
					+ "AND c.statusMonitor = true")
	public List<Computador> listarComputadoresOffline(@Param("limite") LocalDateTime limite);
	
	
	
	

}
