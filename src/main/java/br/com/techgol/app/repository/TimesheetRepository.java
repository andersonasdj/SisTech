package br.com.techgol.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.TimeSheet;
import br.com.techgol.app.orm.TimelineProjecao;

public interface TimesheetRepository extends JpaRepository<TimeSheet, Long> {
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM timesheet t "
					+ "WHERE t.solicitacao_id= :idSolicitacao "
					+ "AND t.funcionario_id=:idFuncionario "
					+ "AND t.status=:status")
	public TimeSheet buscaTimeSheetParaEdicao(Long idSolicitacao, Long idFuncionario, String status );
	
	@Query(nativeQuery = true,
			value = "SELECT SUM(t.duracao) FROM timesheet t WHERE t.funcionario_id=:id AND t.inicio >= :inicio AND t.fim <= :fim")
	public Long buscarMinutosPorFuncionarioPeriodo(Long id, LocalDateTime inicio, LocalDateTime fim);

	
	@Query(nativeQuery = true,
			value = "SELECT t.id, t.inicio, t.fim, t.status, t.duracao, c.nomeCliente, t.solicitacao_id "
			+ "FROM timesheet t "
			+ "INNER JOIN solicitacoes s ON t.solicitacao_id=s.id "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id " 
			+ "WHERE t.funcionario_id=:id "
			+ "AND t.inicio >= :inicio "
			+ "AND t.fim <= :fim "
			+ "ORDER BY t.id")
	public List<TimelineProjecao> listarTimeline(Long id, LocalDateTime inicio, LocalDateTime fim);
	
	
}
