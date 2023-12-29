package br.com.techgol.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.orm.SolicitacaoProjecao;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{
	
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "INNER JOIN funcionarios f ON s.funcionario_id=f.id", nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoes(Pageable page);

}
