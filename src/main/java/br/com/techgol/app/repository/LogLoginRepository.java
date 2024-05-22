package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.LogLogin;

public interface LogLoginRepository extends JpaRepository<br.com.techgol.app.model.LogLogin, Long>{
	
	@Query(nativeQuery = true, value = "SELECT * FROM logLogin s ORDER BY s.id DESC LIMIT 200")
	public List<LogLogin> lstarTodos();

}
