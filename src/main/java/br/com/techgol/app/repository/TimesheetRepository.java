package br.com.techgol.app.repository;

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

}
