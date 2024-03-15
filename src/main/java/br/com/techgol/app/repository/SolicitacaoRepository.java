package br.com.techgol.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.SolicitacaoProjecao;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{
	
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoes(Pageable page, String status, Boolean excluida);

	public int countByLocalAndExcluido(Local local, Boolean excluido);
	public int countByClassificacaoAndExcluido(Classificacao classificacao, Boolean excluido);
	public int countByPrioridadeAndExcluido(Prioridade prioridade, Boolean excluido);
	public int countByStatusAndExcluido(Status status, Boolean excluido);
	public Long countByStatusAndFuncionarioId(Status status, Long id);
	public int countByFuncionarioIdAndExcluido(long id, Boolean excluido);

}
