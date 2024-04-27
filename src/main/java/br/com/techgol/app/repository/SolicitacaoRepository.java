package br.com.techgol.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.DtoUltimaAtualizada;
import br.com.techgol.app.orm.PojecaoResumidaFinalizados;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.orm.SolicitacaoProjecaoCompleta;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{
	
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, s.classificacao, s.descricao, "
			+ "s.formaAbertura, c.redFlag, s.local, s.observacao, s.prioridade, s.resolucao, c.vip, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, "
			+ "s.dataAbertura, s.dataAtualizacao, s.dataAgendado "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAgendado >= :inicio "
			+ "AND s.dataAgendado <= :fim")
	public List<SolicitacaoProjecaoEntidadeComAtributos> listarSolicitacoesAgendadasDoDia(Status status, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, s.classificacao, s.descricao, "
			+ "s.formaAbertura, c.redFlag, s.local, s.observacao, s.prioridade, s.resolucao, c.vip, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, "
			+ "s.dataAbertura, s.dataAtualizacao, s.dataAgendado "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAgendado < :inicio")
	public List<SolicitacaoProjecaoEntidadeComAtributos> listarSolicitacoesAgendadasAtrasadas(Status status, Boolean excluida, LocalDateTime inicio);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(s.id) "
			+ "FROM solicitacoes s "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAgendado < :inicio")
	public Long listarSolicitacoesAgendadasAtrasadasQtd(Status status, Boolean excluida, LocalDateTime inicio);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(s.id) "
			+ "FROM solicitacoes s "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAgendado >= :inicio "
			+ "AND s.dataAgendado <= :fim")
	public Long listarSolicitacoesAgendadasHojeQtd(Status status, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(s.id) "
			+ "FROM solicitacoes s "
			+ "WHERE s.excluido = :excluida "
			+ "AND s.dataAtualizacao >= :inicio "
			+ "AND s.dataAtualizacao <= :fim")
	public Long listarSolicitacoesAtualizadasHojeQtd(Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(s.id) "
			+ "FROM solicitacoes s "
			+ "WHERE s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public Long listarSolicitacoesAbertasHojeQtd(Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(s.id) "
			+ "FROM solicitacoes s "
			+ "WHERE s.excluido = :excluida "
			+ "AND s.status = :status "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim")
	public Long listarSolicitacoesFinalizadasHojeQtd(String status, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, c.nomeCliente, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.status, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.solicitante, "
			+ "f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao, s.dataAgendado, s.log_id "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoes(Pageable page, String status, Boolean excluida);
	
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida",nativeQuery = true)
	public List<SolicitacaoProjecao> listarSolicitacoesNaoFinalizadas(String status, Boolean excluida);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.duracao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.cliente_id=:cliente_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public List<SolicitacaoProjecao> listarSolicitacoesPorDataCsv(Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.duracao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.funcionario_id=:funcionario_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public List<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioDataCsv(Long funcionario_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.cliente_id=:cliente_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorDataRelatorio(Pageable page, Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAgendado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorPeriodoRelatorio(Pageable page, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.funcionario_id=:funcionario_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioData(Pageable page, Long funcionario_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id,  "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAgendado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.excluido = false "
			+ "AND s.dataAtualizacao >= :hoje "
			+ "AND s.dataAtualizacao <= :hojeFim")
	public Page<SolicitacaoProjecao> listarSolicitacoesAtualizadasHoje(Pageable page, LocalDateTime hoje, LocalDateTime hojeFim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.excluido = false "
			+ "AND s.status = 'FINALIZADO' "
			+ "AND s.dataFinalizado >= :hojeInicio "
			+ "AND s.dataFinalizado <= :hojeFim")
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadasHoje(Pageable page, LocalDateTime hojeInicio, LocalDateTime hojeFim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAgendado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.excluido = false "
			+ "AND s.dataAbertura >= :hojeInicio "
			+ "AND s.dataAbertura <= :hojeFim")
	public Page<SolicitacaoProjecao> listarSolicitacoesAbertasHoje(Pageable page, LocalDateTime hojeInicio, LocalDateTime hojeFim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, s.status, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.solicitante, "
			+ "s.dataAgendado, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.excluido = false "
			+ "AND s.status = 'AGENDADO' "
			+ "AND s.dataAgendado >= :hojeInicio "
			+ "AND s.dataAgendado <= :hojeFim")
	public Page<SolicitacaoProjecao> listarSolicitacoesAgendadasHoje(Pageable page, LocalDateTime hojeInicio, LocalDateTime hojeFim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, s.status, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.solicitante, "
			+ "s.dataAgendado, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.excluido = false "
			+ "AND s.status = 'AGENDADO' "
			+ "AND s.dataAgendado < :hojeInicio")
	public Page<SolicitacaoProjecao> listarSolicitacoesAgendadasAtrasadas(Pageable page, LocalDateTime hojeInicio);
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, c.redFlag, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status = :status " 
			+ "AND s.excluido = :excluida",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadas(Pageable page, String status, Boolean excluida);
	
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, "
			+ "s.dataAbertura, s.duracao, s.dataAndamento, s.dataFinalizado, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.id = :id "
			+ "AND s.status = :status " 
			+ "AND s.excluido = false ",nativeQuery = true)
	public SolicitacaoProjecaoCompleta buscarSolicitacaoFinalizada(Long id, String status);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, s.formaAbertura, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, "
			+ "s.dataAbertura, s.duracao, s.dataAndamento, s.dataFinalizado, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.id = :id "
			+ "AND s.excluido = false ")
	public SolicitacaoProjecaoCompleta buscarSolicitacaoRelatorio(Long id);
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, "
			+ "s.duracao, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status = 'FINALIZADO' "
			+ "AND s.cliente_id = :id "
			+ "AND s.excluido = false",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadasPorCliente(Pageable page, Long id);
	
	
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, "
			+ "s.duracao, s.dataAtualizacao, s.dataAgendado "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != 'FINALIZADO' "
			+ "AND s.cliente_id = :id "
			+ "AND s.excluido = false",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoesNaoFinalizadasPorCliente(Pageable page, Long id);
	
	
	public int countByStatusAndExcluido(Status status, Boolean excluido);
	

	public Long countByStatusAndFuncionarioId(Status status, Long id);

	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.local = :local and s.excluido = :excluido and s.status != :status", nativeQuery = true)
	public int totalPorLocal(String local, Boolean excluido, String status);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.formaAbertura = :formaAbertura and s.excluido = :excluido and s.status != :status", nativeQuery = true)
	public int totalPorFormaAbertura(String formaAbertura, Boolean excluido, String status);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.classificacao = :classificacao and s.excluido = :excluido and s.status != :status", nativeQuery = true)
	public int totalPorClassificacao(String classificacao, Boolean excluido, String status);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.prioridade = :prioridade and s.excluido = :excluido and s.status != :status", nativeQuery = true)
	public int totalPorPrioridade(String prioridade, Boolean excluido, String status);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.funcionario_id = :id and s.excluido = :excluido and s.status != :status", nativeQuery = true)
	public int totalPorFuncionario(long id, Boolean excluido, String status);
	
	//#######################################################################################################
	
	public int countByClienteIdAndStatusAndExcluido(Long id, Status status, Boolean excluido);
	
	public int countByClienteIdAndExcluidoAndDataAberturaAfter(Long id, Boolean excluido, LocalDateTime dataAbertura);
	
	public List<PojecaoResumidaFinalizados> findByClienteIdAndExcluidoAndStatusAndDataFinalizadoAfter(Long id, Boolean excluido, Status status, LocalDateTime dataFechamento);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO'AND s.cliente_id = :id AND s.local = :local AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorLocalPorCliente(Long id, String local, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.cliente_id = :id AND s.formaAbertura = :formaAbertura and s.excluido = :excluido and s.status != 'FINALIZADO'", nativeQuery = true)
	public int totalPorFormaAberturaPorCliente(Long id, String formaAbertura, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO' AND s.cliente_id = :id AND s.classificacao = :classificacao AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorClassificacaoPorCliente(Long id, String classificacao, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO' AND s.cliente_id = :id AND s.prioridade = :prioridade AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorPrioridadePorCliente(Long id, String prioridade, Boolean excluido);

	
	@Query(value = "SELECT s.id, s.dataAtualizacao FROM solicitacoes s WHERE s.excluido = 'false' AND s.status != 'FINALIZADO' ORDER BY s.dataAtualizacao DESC LIMIT 1", nativeQuery = true)
	public DtoUltimaAtualizada buscaUltimaAtualizada();

	@Query(nativeQuery = true,
			value = "SELECT * FROM solicitacoes s WHERE s.excluido = 'false' AND s.status = 'ANDAMENTO'")
	public List<Solicitacao> buscaSolicitacoesEmAndamento();


	
	

}
