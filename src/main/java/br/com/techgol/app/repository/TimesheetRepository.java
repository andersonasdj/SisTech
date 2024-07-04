package br.com.techgol.app.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.TimeSheet;

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

}
