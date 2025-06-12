package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.Computador;

public interface ComputadorRepository extends JpaRepository<Computador, Long> {

	@Query(value = "SELECT * FROM computador c", nativeQuery = true)
	public List<Computador> listarTodos();

}
