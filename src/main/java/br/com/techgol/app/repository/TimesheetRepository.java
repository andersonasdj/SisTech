package br.com.techgol.app.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.TimeSheet;
import br.com.techgol.app.orm.TimelineProjecao;
import br.com.techgol.app.orm.TimesheetProjecao;

public interface TimesheetRepository extends JpaRepository<TimeSheet, Long> {

	@Query(nativeQuery = true,
			value = "SELECT * FROM timesheet t WHERE t.funcionario_id=:id AND t.inicio >= :inicio AND t.fim <= :fim")
	public List<TimeSheet> buscarTimesheetPorFuncionarioPeriodo(Long id, LocalDateTime inicio, LocalDateTime fim);
	
	public void deleteBySolicitacao_idAndFuncionario_id(Long solicitacao_id, Long funcionario_id);
	
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
			value = "SELECT t.id, t.inicio, t.fim, t.status, t.duracao, c.nomeCliente, t.solicitacao_id, s.local "
			+ "FROM timesheet t "
			+ "INNER JOIN solicitacoes s ON t.solicitacao_id=s.id "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id " 
			+ "WHERE t.funcionario_id=:id "
			+ "AND t.inicio >= :inicio "
			+ "AND t.fim <= :fim "
			+ "ORDER BY t.id")
	public List<TimelineProjecao> listarTimeline(Long id, LocalDateTime inicio, LocalDateTime fim);

	public void deleteBySolicitacaoId(Long id);

	public boolean existsBySolicitacaoId(Long id);

	public List<TimeSheet> findBySolicitacaoId(Long id);
	
	@Query(nativeQuery = true,
			value = "SELECT t.id, t.inicio, t.fim, t.status, t.duracao, f.nomeFuncionario "
			+ "FROM timesheet t "
			+ "INNER JOIN funcionarios f ON t.funcionario_id=f.id "
			+ "WHERE t.solicitacao_id=:id "
			+ "ORDER BY t.id")
	public Page<TimesheetProjecao> listarTimesheetProjecao(Pageable page, Long id);

	@Query(nativeQuery = true,
			value = "SELECT SUM(t.duracao) FROM timesheet t "
			+ "WHERE t.funcionario_id = :id "
			+ "AND t.inicio >= :inicio "
			+ "AND t.fim <= :fim")
	public Long totalHorasPeriodoPorFuncionario(Long id, LocalDateTime inicio, LocalDateTime fim);

	@Query(nativeQuery = true,
			value = "SELECT t.solicitacao_id "
			+ "FROM timesheet t "
			+ "WHERE t.id=:id")
	public Long buscaPorId(Long id);

	
	@Query(nativeQuery = true,
			value = "SELECT SUM(t.duracao) FROM timesheet t "
			+ "WHERE t.solicitacao_id=:id ")
	public Long bucarDuracaoIdSolicitacao(Long id);
	
	
	@Query(nativeQuery = true,
			value = "SELECT SUM(t.duracao) FROM timesheet t "
			+ "WHERE t.idcliente = :id "
			+ "AND t.inicio >= :inicio "
			+ "AND t.fim <= :fim "
			+ "AND t.funcionario_id = :funcionarioId")
	public BigDecimal custoOperacionalTecPorCliente(Long id, LocalDateTime inicio, LocalDateTime fim, Long funcionarioId);
}
