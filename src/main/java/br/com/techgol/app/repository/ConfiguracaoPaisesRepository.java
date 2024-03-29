package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.ConfiguracaoPaises;

public interface ConfiguracaoPaisesRepository extends JpaRepository<ConfiguracaoPaises, Long> {
	
	
	@Query(value = "SELECT COUNT(*) FROM configPaises", nativeQuery = true)
	public int existsConfigPaises();
	
	@Query(value = "SELECT * FROM configPaises p ORDER BY p.pais", nativeQuery = true)
	public List<ConfiguracaoPaises> listarPaises();
	
	
	@Query(value = "SELECT * FROM configPaises p ORDER BY p.pais", nativeQuery = true)
	public List<ConfiguracaoPaises> listarPaisesCompleto();
	
	@Query(value = "SELECT p.pais FROM configPaises p WHERE p.status = '1' ORDER BY p.pais", nativeQuery = true)
	public List<String> listarPaisesString();
	
	public ConfiguracaoPaises findByPais(String pais);

}
