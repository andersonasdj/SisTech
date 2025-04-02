package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.ConjuntoModelos;
import br.com.techgol.app.model.ModeloSolicitacao;

public interface ModeloSolicitacaoRepository extends JpaRepository<ModeloSolicitacao, Long>{

	@Query(nativeQuery = true, 
			value = "SELECT s.id, s.categoria, s.classificacao, s.descricao, "
					+ "s.formaAbertura, s.local, s.observacao, s.prioridade, "
					+ "s.status, s.conjuntoModelos_id "
					+ "FROM modelo_solicitacoes s "
					+ "WHERE s.conjuntoModelos_id=:id")
	List<ModeloSolicitacao> buscarSolocitacoesModelosPorIdConjunto(Long id);
	
	
	@Query(nativeQuery = true, 
			value = "SELECT s.id, s.categoria, s.classificacao, s.descricao, "
					+ "s.formaAbertura, s.local, s.observacao, s.prioridade, "
					+ "s.status, s.conjuntoModelos_id "
					+ "FROM modelo_solicitacoes s "
					+ "WHERE s.id=:id")
	ModeloSolicitacao buscarSolocitacaoModeloPorId(Long id);


	void deleteByConjuntoModelos(ConjuntoModelos buscaPorId);


}
