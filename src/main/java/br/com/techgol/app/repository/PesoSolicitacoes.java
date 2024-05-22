package br.com.techgol.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.PesoSolicitacao;

public interface PesoSolicitacoes extends JpaRepository<PesoSolicitacao, Long>{
	
	public PesoSolicitacao findPesoByItem(String item);
	
	@Query(value = "SELECT COUNT(*) FROM peso_solicitacoes", nativeQuery = true)
	public int existsConfigPesos();

}
