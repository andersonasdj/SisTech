package br.com.techgol.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.TimeRefeicao;
import br.com.techgol.app.orm.RefeicaoProjecao;

public interface TimeRefeicaoRepository extends JpaRepository<TimeRefeicao, Long> {

	
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) "
			+ "FROM timerefeicao tr "
			+ "WHERE tr.funcionario_id=:id "
			+ "AND tr.inicio >= :inicio "
			+ "AND tr.fim <= :fim")
	long existeUpdateHoje(Long id, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM timerefeicao tr "
			+ "WHERE tr.funcionario_id=:id "
			+ "AND tr.inicio >= :inicio "
			+ "AND tr.inicio <= :fim "
			+ "LIMIT 1")
	TimeRefeicao existeFlagRefeicaoHoje(Long id, LocalDateTime inicio, LocalDateTime fim);

	
	@Query(nativeQuery = true,
			value = "SELECT tf.id, tf.inicio, tf.fim, tf.duracao, f.nomeFuncionario "
			+ "FROM timerefeicao tf "
			+ "INNER JOIN funcionarios f ON tf.funcionario_id=f.id "
			+ "ORDER BY tf.id DESC "
			+ "LIMIT 50")
	List<RefeicaoProjecao> listarUltimos50();

}
