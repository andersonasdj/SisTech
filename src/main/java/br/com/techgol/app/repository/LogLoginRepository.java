package br.com.techgol.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.LogLogin;

public interface LogLoginRepository extends JpaRepository<br.com.techgol.app.model.LogLogin, Long>{
	
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM logLogin s ORDER BY s.id DESC")
	public Page<LogLogin> lstarTodos(Pageable page);

}
