package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.ConjuntoModelos;

public interface ConjuntoModelosRepository extends JpaRepository<ConjuntoModelos, Long> {
	
	
	@Query(nativeQuery = true, value = "select c.id, c.nomeModelo FROM conjunto_modelos c WHERE c.id=:id")
	public ConjuntoModelos buscaPorId(Long id);

	
	@Query(value = "SELECT m.nomeModelo FROM conjunto_modelos m ORDER BY m.id", nativeQuery = true)
	public List<String> listarNomesModelos();

	@Query(value = "SELECT m.id FROM conjunto_modelos m ORDER BY m.id", nativeQuery = true)
	public List<String> listarIdModelos();

}
