package br.com.techgol.app.repository.legacy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.legacy.SolicitacaoLegacy;
import br.com.techgol.app.orm.SolicitacaoLegacyProjecao;

public interface SolicitacaoLegacyRepository extends JpaRepository<SolicitacaoLegacy, Long>{
	
	
	@Query(value = "SELECT s.id, s.abriuChamado, s.Agendado, s.dataAbertura, "
			+ "s.descricaoProblema, s.formaAbertura, s.nivelDeIncidencia, "
			+ "s.obs, s.onsiteOffsite, s.prioridade, s.resolucao, "
			+ "s.solicitante, s.status, s.usuario, c.nome FROM Solicitacao s "
			+ "INNER JOIN Cliente c ON s.cliente_id=c.id", nativeQuery = true)
	public Page<SolicitacaoLegacyProjecao> listarSolicitacoes(Pageable page);
	
	
	
	@Query(value = "SELECT s.id, s.abriuChamado, s.Agendado, s.dataAbertura, "
			+ "s.descricaoProblema, s.formaAbertura, s.nivelDeIncidencia, "
			+ "s.obs, s.onsiteOffsite, s.prioridade, s.resolucao, "
			+ "s.solicitante, s.status, s.usuario, c.nome FROM Solicitacao s "
			+ "INNER JOIN Cliente c ON s.cliente_id=c.id "
			+ "WHERE s.id=:id", nativeQuery = true)
	public SolicitacaoLegacyProjecao listarSolicitacaoLegacyPorId(Long id);
	
	
	public Page<SolicitacaoLegacy> findByUsuarioLike(String usuario, Pageable page);

}
