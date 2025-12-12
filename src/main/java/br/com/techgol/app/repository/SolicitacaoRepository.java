package br.com.techgol.app.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.DtoUltimaAtualizada;
import br.com.techgol.app.orm.PojecaoResumidaFinalizados;
import br.com.techgol.app.orm.ProjecaoDadosImpressao;
import br.com.techgol.app.orm.ProjecaoDashboardGerencia;
import br.com.techgol.app.orm.SolicitacaoProjecao;
import br.com.techgol.app.orm.SolicitacaoProjecaoCompleta;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{
	
	@Query(nativeQuery = true,
			value = "SELECT SUM(s.duracao) FROM solicitacoes s "
			+ "WHERE s.excluido = false "
			+ "AND s.cliente_id = :id "
			+ "AND s.status = 'FINALIZADO' "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim")
	public Long totalHorasPeriodoPorCliente(Long id, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
					+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
					+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAndamento, s.dataAgendado, "
					+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
					+ "FROM solicitacoes s "
					+ "INNER JOIN clientes c ON s.cliente_id=c.id "
					+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
					+ "WHERE s.formaAbertura LIKE CONCAT('%', :abertura ,'%') "
					+ "AND s.categoria LIKE CONCAT('%', :categoria, '%') "
					+ "AND s.classificacao LIKE CONCAT('%', :classificacao, '%') "
					+ "AND s.local LIKE CONCAT('%', :local, '%') "
					+ "AND s.prioridade LIKE CONCAT('%', :prioridade, '%') "
					+ "AND s.status LIKE CONCAT('%', :status, '%') "
					+ "AND s.excluido = :excluida "
					+ "AND s.dataAbertura >= :inicio "
					+ "AND s.dataAbertura <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorPeriodoRelatorioAbertura(Pageable page, Boolean excluida, LocalDateTime inicio, LocalDateTime fim, 
			String abertura, String categoria, String classificacao, String local, String prioridade, String status);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAndamento, s.dataAgendado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.formaAbertura LIKE CONCAT('%', :abertura, '%') "
			+ "AND s.categoria LIKE CONCAT('%', :categoria, '%') "
			+ "AND s.classificacao LIKE CONCAT('%', :classificacao, '%') "
			+ "AND s.local LIKE CONCAT('%', :local, '%') "
			+ "AND s.prioridade LIKE CONCAT('%', :prioridade, '%') "
			+ "AND s.status = 'FINALIZADO' "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorPeriodoRelatorioFechamento(Pageable page, Boolean excluida, LocalDateTime inicio, LocalDateTime fim, 
			String abertura, String categoria, String classificacao, String local, String prioridade);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
					+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
					+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAndamento, s.dataAgendado, "
					+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
					+ "FROM solicitacoes s "
					+ "INNER JOIN clientes c ON s.cliente_id=c.id "
					+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
					+ "WHERE s.formaAbertura LIKE CONCAT('%', :abertura, '%') "
					+ "AND s.categoria LIKE CONCAT('%', :categoria, '%') "
					+ "AND s.classificacao LIKE CONCAT('%', :classificacao, '%') "
					+ "AND s.local LIKE CONCAT('%', :local, '%') "
					+ "AND s.status LIKE CONCAT('%', :status, '%') "
					+ "AND s.prioridade LIKE CONCAT('%', :prioridade, '%') "
					+ "AND s.excluido = :excluida "
					+ "AND s.dataAtualizacao >= :inicio "
					+ "AND s.dataAtualizacao <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorPeriodoRelatorioAtualizado(Pageable page, Boolean excluida, LocalDateTime inicio, LocalDateTime fim, 
			String abertura, String categoria, String classificacao, String local, String prioridade, String status);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
					+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
					+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, "
					+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
					+ "FROM solicitacoes s "
					+ "INNER JOIN clientes c ON s.cliente_id=c.id "
					+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
					+ "WHERE s.formaAbertura LIKE CONCAT('%', :abertura, '%') "
					+ "AND s.categoria LIKE CONCAT('%', :categoria, '%') "
					+ "AND s.classificacao LIKE CONCAT('%', :classificacao, '%') "
					+ "AND s.local LIKE CONCAT('%', :local, '%') "
					+ "AND s.prioridade LIKE CONCAT('%', :prioridade, '%') "
					+ "AND s.funcionario_id LIKE :funcionario_id "
					+ "AND s.cliente_id=:cliente_id "
					+ "AND s.excluido = :excluida "
					+ "AND s.dataAtualizacao >= :inicio "
					+ "AND s.dataAtualizacao <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorDataRelatorioAtualizadoFiltro(Pageable page, Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim, 
			String abertura, String categoria, String classificacao, String local, String prioridade, String funcionario_id);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
					+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
					+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, "
					+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
					+ "FROM solicitacoes s "
					+ "INNER JOIN clientes c ON s.cliente_id=c.id "
					+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
					+ "WHERE s.formaAbertura LIKE CONCAT('%', :abertura,'%') "
					+ "AND s.categoria LIKE CONCAT('%', :categoria, '%') "
					+ "AND s.classificacao LIKE CONCAT('%', :classificacao, '%') "
					+ "AND s.local LIKE CONCAT('%', :local, '%') "
					+ "AND s.prioridade LIKE CONCAT('%', :prioridade, '%') "
					+ "AND s.funcionario_id LIKE CONCAT('%', :funcionario_id, '%') "
					+ "AND s.cliente_id=:cliente_id "
					+ "AND s.excluido = :excluida "
					+ "AND s.dataAbertura >= :inicio "
					+ "AND s.dataAbertura <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorDataRelatorioAberturaFiltro(Pageable page, Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim, 
			String abertura, String categoria, String classificacao, String local, String prioridade, String funcionario_id);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.formaAbertura LIKE CONCAT('%', :abertura, '%') "
			+ "AND s.categoria LIKE CONCAT('%', :categoria, '%') "
			+ "AND s.classificacao LIKE CONCAT('%', :classificacao, '%') "
			+ "AND s.local LIKE CONCAT('%', :local, '%') "
			+ "AND s.prioridade LIKE CONCAT('%', :prioridade, '%') "
			+ "AND s.funcionario_id LIKE CONCAT('%', :funcionario_id, '%') "
			+ "AND s.cliente_id=:cliente_id "
			+ "AND s.status = 'FINALIZADO' "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorDataRelatorioFechamentoFiltro(Pageable page, Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim, 
			String abertura, String categoria, String classificacao, String local, String prioridade, String funcionario_id);
	
	@Query(value = "SELECT s.id, s.dataAbertura "
			+ "FROM solicitacoes s "
			+ "WHERE s.excluido = false " 
			+ "AND s.dataAbertura >= :data",nativeQuery = true)
	public List<ProjecaoDashboardGerencia> buscaDadosDashboardGerencia(LocalDate data);
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, c.nomeCliente, s.duracao, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.status, s.peso, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.solicitante, s.versao, "
			+ "f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao, s.dataAgendado, s.log_id "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.descricao COLLATE utf8mb4_general_ci LIKE CONCAT('%', :palavra, '%') "
			+ "OR s.observacao COLLATE utf8mb4_general_ci LIKE CONCAT('%', :palavra, '%') "
			+ "OR s.resolucao COLLATE utf8mb4_general_ci LIKE CONCAT('%', :palavra, '%') "
			+ "OR s.id LIKE CONCAT('%', :palavra, '%') "
			+ "ORDER BY s.id DESC" ,nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoesPorPalavra(Pageable page, String palavra);
	
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
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, c.nomeCliente, s.duracao, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.status, s.peso, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.solicitante, s.versao, "
			+ "f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao, s.dataAgendado, s.log_id, s.dataAndamento "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoes(Pageable page, String status, Boolean excluida);
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, c.nomeCliente, s.peso, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.status, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.solicitante, s.versao, "
			+ "f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao, s.dataAgendado, s.log_id, s.dataAndamento "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != 'FINALIZADO' "
			+ "AND s.status = :status " 
			+ "AND s.excluido = :excluida",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoesPorStatus(Pageable page, String status, Boolean excluida);
	
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
	
	@Query(
			  nativeQuery = true,
			  value = """
			    SELECT s.id,
			           s.abertoPor,
			           s.afetado,
			           s.categoria,
			           s.classificacao,
			           s.descricao,
			           s.formaAbertura,
			           c.redFlag,
			           s.dataFinalizado,
			           s.local,
			           s.observacao,
			           s.prioridade,
			           s.resolucao,
			           c.vip,
			           s.versao,
			           s.duracao,
			           s.solicitante,
			           s.status,
			           c.nomeCliente,
			           f.nomeFuncionario,
			           s.dataAbertura,
			           s.dataAtualizacao
			    FROM solicitacoes s
			    INNER JOIN clientes c ON s.cliente_id = c.id
			    LEFT JOIN funcionarios f ON s.funcionario_id = f.id
			    WHERE s.formaAbertura LIKE CONCAT('%', :abertura, '%')
			      AND s.categoria     LIKE CONCAT('%', :categoria, '%')
			      AND s.classificacao LIKE CONCAT('%', :classificacao, '%')
			      AND s.local         LIKE CONCAT('%', :local, '%')
			      AND s.prioridade    LIKE CONCAT('%', :prioridade, '%')
			      AND (:clienteId IS NULL OR s.cliente_id = :clienteId)
			      AND s.excluido = :excluida
			      AND s.dataAbertura >= :inicio
			      AND s.dataAbertura <= :fim
			    """
			)
			List<SolicitacaoProjecao> listarSolicitacoesPorPeriodoAberturaDataCsv(
			    @Param("clienteId") Long clienteId,
			    @Param("excluida") Boolean excluida,
			    @Param("inicio") LocalDateTime inicio,
			    @Param("fim") LocalDateTime fim,
			    @Param("abertura") String abertura,
			    @Param("categoria") String categoria,
			    @Param("classificacao") String classificacao,
			    @Param("local") String local,
			    @Param("prioridade") String prioridade
			);

	
//	@Query(nativeQuery = true,
//			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
//			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.dataFinalizado, "
//			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.duracao, "
//			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
//			+ "FROM solicitacoes s "
//			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
//			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
//			+ "WHERE s.formaAbertura LIKE CONCAT('%', :abertura, '%') "
//			+ "AND s.categoria LIKE CONCAT('%', :categoria, '%') "
//			+ "AND s.classificacao LIKE CONCAT('%', :classificacao, '%') "
//			+ "AND s.local LIKE CONCAT('%', :local, '%') "
//			+ "AND s.prioridade LIKE CONCAT('%', :prioridade, '%') "
//			+ "AND s.cliente_id=:cliente_id "
//			+ "AND s.excluido = :excluida "
//			+ "AND s.dataAbertura >= :inicio "
//			+ "AND s.dataAbertura <= :fim")
//	public List<SolicitacaoProjecao> listarSolicitacoesPorPeriodoAberturaDataCsv(Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim,
//			String abertura, String categoria, String classificacao, String local, String prioridade);
	
	@Query(
			  nativeQuery = true,
			  value = """
			    SELECT s.id,
			           s.abertoPor,
			           s.afetado,
			           s.categoria,
			           s.classificacao,
			           s.descricao,
			           s.formaAbertura,
			           c.redFlag,
			           s.dataFinalizado,
			           s.local,
			           s.observacao,
			           s.prioridade,
			           s.resolucao,
			           c.vip,
			           s.versao,
			           s.duracao,
			           s.solicitante,
			           s.status,
			           c.nomeCliente,
			           f.nomeFuncionario,
			           s.dataAbertura,
			           s.dataAtualizacao
			    FROM solicitacoes s
			    INNER JOIN clientes c ON s.cliente_id = c.id
			    LEFT JOIN funcionarios f ON s.funcionario_id = f.id
			    WHERE (:clienteId IS NULL OR s.cliente_id = :clienteId)
			      AND s.formaAbertura LIKE CONCAT('%', :abertura, '%')
			      AND s.categoria     LIKE CONCAT('%', :categoria, '%')
			      AND s.classificacao LIKE CONCAT('%', :classificacao, '%')
			      AND s.local         LIKE CONCAT('%', :local, '%')
			      AND s.prioridade    LIKE CONCAT('%', :prioridade, '%')
			      AND s.status = 'FINALIZADO'
			      AND s.excluido = :excluida
			      AND s.dataFinalizado >= :inicio
			      AND s.dataFinalizado <= :fim
			    """)
	List<SolicitacaoProjecao> listarSolicitacoesPorPeriodoFechamentoDataCsv(
		    @Param("clienteId") Long clienteId,
		    @Param("excluida") Boolean excluida,
		    @Param("inicio") LocalDateTime inicio,
		    @Param("fim") LocalDateTime fim,
		    @Param("abertura") String abertura,
		    @Param("categoria") String categoria,
		    @Param("classificacao") String classificacao,
		    @Param("local") String local,
		    @Param("prioridade") String prioridade
		);
	
	@Query(
			  nativeQuery = true,
			  value = """
			    SELECT s.id,
			           s.abertoPor,
			           s.afetado,
			           s.categoria,
			           s.classificacao,
			           s.descricao,
			           s.formaAbertura,
			           c.redFlag,
			           s.dataFinalizado,
			           s.local,
			           s.observacao,
			           s.prioridade,
			           s.resolucao,
			           c.vip,
			           s.versao,
			           s.duracao,
			           s.solicitante,
			           s.status,
			           c.nomeCliente,
			           f.nomeFuncionario,
			           s.dataAbertura,
			           s.dataAtualizacao
			    FROM solicitacoes s
			    INNER JOIN clientes c ON s.cliente_id = c.id
			    LEFT JOIN funcionarios f ON s.funcionario_id = f.id
			    WHERE s.formaAbertura LIKE CONCAT('%', :abertura, '%')
			      AND s.categoria     LIKE CONCAT('%', :categoria, '%')
			      AND s.classificacao LIKE CONCAT('%', :classificacao, '%')
			      AND s.local         LIKE CONCAT('%', :local, '%')
			      AND s.prioridade    LIKE CONCAT('%', :prioridade, '%')
			      AND (:clienteId IS NULL OR s.cliente_id = :clienteId)
			      AND s.excluido = :excluida
			      AND s.dataAtualizacao >= :inicio
			      AND s.dataAtualizacao <= :fim
			    """
			)
			List<SolicitacaoProjecao> listarSolicitacoesPorPeriodoAtualizadoDataCsv(
			    @Param("clienteId") Long clienteId,
			    @Param("excluida") Boolean excluida,
			    @Param("inicio") LocalDateTime inicio,
			    @Param("fim") LocalDateTime fim,
			    @Param("abertura") String abertura,
			    @Param("categoria") String categoria,
			    @Param("classificacao") String classificacao,
			    @Param("local") String local,
			    @Param("prioridade") String prioridade
			);

	
	
//	@Query(nativeQuery = true,
//			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
//			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.dataFinalizado, "
//			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.duracao, "
//			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
//			+ "FROM solicitacoes s "
//			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
//			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
//			+ "WHERE s.formaAbertura LIKE CONCAT('%', :abertura, '%') "
//			+ "AND s.categoria LIKE CONCAT('%', :categoria, '%') "
//			+ "AND s.classificacao LIKE CONCAT('%', :classificacao, '%') "
//			+ "AND s.local LIKE CONCAT('%', :local, '%') "
//			+ "AND s.prioridade LIKE CONCAT('%', :prioridade, '%') "
//			+ "AND s.funcionario_id LIKE CONCAT('%', :funcionario_id, '%') "
//			+ "AND s.cliente_id=:cliente_id "
//			+ "AND s.excluido = :excluida "
//			+ "AND s.dataAtualizacao >= :inicio "
//			+ "AND s.dataAtualizacao <= :fim")
//	public List<SolicitacaoProjecao> listarSolicitacoesPorPeriodoAtualizadoDataCsv(Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim,
//			String abertura, String categoria, String classificacao, String local, String prioridade);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.dataFinalizado, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.duracao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public List<SolicitacaoProjecao> listarSolicitacoesPorPeriodoDataCsv(Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.duracao, s.dataFinalizado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.funcionario_id=:funcionario_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public List<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioDataAberturaCsv(Long funcionario_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.duracao, s.dataFinalizado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.funcionario_id=:funcionario_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim")
	public List<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioDataFechamentoCsv(Long funcionario_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.dataFinalizado, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.duracao, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.funcionario_id=:funcionario_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAtualizacao >= :inicio "
			+ "AND s.dataAtualizacao <= :fim")
	public List<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioDataAtualizadoCsv(Long funcionario_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAndamento, s.dataAgendado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.cliente_id=:cliente_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorDataRelatorioAbertura(Pageable page, Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAndamento, s.dataAgendado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.cliente_id=:cliente_id "
			+ "AND s.status = 'FINALIZADO' "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorDataRelatorioFechamento(Pageable page, Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAndamento, s.dataAgendado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.cliente_id=:cliente_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAtualizacao >= :inicio "
			+ "AND s.dataAtualizacao <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorDataRelatorioAtualizado(Pageable page, Long cliente_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
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
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.anexo, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAgendado, s.dataAndamento, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.funcionario_id=:funcionario_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioDataAbertura(Pageable page, Long funcionario_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.anexo, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAgendado, s.dataAndamento, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.funcionario_id=:funcionario_id "
			+ "AND s.status = 'FINALIZADO' "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioDataFechamento(Pageable page, Long funcionario_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.anexo, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAgendado, s.dataAndamento, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.funcionario_id=:funcionario_id "
			+ "AND s.excluido = :excluida "
			+ "AND s.dataAtualizacao >= :inicio "
			+ "AND s.dataAtualizacao <= :fim")
	public Page<SolicitacaoProjecao> listarSolicitacoesPorFuncionarioDataAtualizado(Pageable page, Long funcionario_id, Boolean excluida, LocalDateTime inicio, LocalDateTime fim);

	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
			+ "WHERE s.funcionario_id = :id "
			+ "AND s.excluido = :excluido "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public int totalabertasPeriodoPorFuncionario(Long id, Boolean excluido, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
			+ "WHERE s.funcionario_id = :id "
			+ "AND s.excluido = :excluido "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim "
			+ "AND s.status = 'FINALIZADO'")
	public int totalFechadasPeriodoPorFuncionario(Long id, Boolean excluido, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
			+ "WHERE s.funcionario_id = :id "
			+ "AND s.excluido = :excluido "
			+ "AND s.dataAtualizacao >= :inicio "
			+ "AND s.dataAtualizacao <= :fim")
	public int totalAtualizadosPeriodoPorFuncionario(Long id, Boolean excluido, LocalDateTime inicio, LocalDateTime fim);
	
	//##
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
			+ "WHERE s.cliente_id = :id "
			+ "AND s.excluido = :excluido "
			+ "AND s.dataAbertura >= :inicio "
			+ "AND s.dataAbertura <= :fim")
	public int totalabertasPeriodoPorCliente(Long id, Boolean excluido, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
			+ "WHERE s.cliente_id = :id "
			+ "AND s.excluido = :excluido "
			+ "AND s.dataFinalizado >= :inicio "
			+ "AND s.dataFinalizado <= :fim "
			+ "AND s.status = 'FINALIZADO'")
	public int totalFechadasPeriodoPorCliente(Long id, Boolean excluido, LocalDateTime inicio, LocalDateTime fim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
			+ "WHERE s.cliente_id = :id "
			+ "AND s.excluido = :excluido "
			+ "AND s.dataAtualizacao >= :inicio "
			+ "AND s.dataAtualizacao <= :fim")
	public int totalAtualizadosPeriodoPorCliente(Long id, Boolean excluido, LocalDateTime inicio, LocalDateTime fim);
	//##
	
	@Query(nativeQuery = true,
			value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, s.duracao, s.log_id,  "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, s.versao, s.dataAgendado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao, s.dataAndamento "
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
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao, s.dataAndamento "
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
			+ "AND s.excluido = :excluida ORDER BY s.id DESC LIMIT 200",nativeQuery = true)
	public List<SolicitacaoProjecao> listarSolicitacoesFinalizadas(String status, Boolean excluida);
	
	
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
			+ "s.classificacao, s.descricao, s.formaAbertura, s.cliente_id, s.anexo, "
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
			+ "s.duracao, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status = 'FINALIZADO' "
			+ "AND s.funcionario_id = :id "
			+ "AND s.excluido = false",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoesFinalizadasPorFuncionario(Pageable page, Long id);
	
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
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, c.redFlag, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, "
			+ "s.duracao, s.dataAtualizacao, s.dataAgendado "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != 'FINALIZADO' "
			+ "AND s.funcionario_id = :id "
			+ "AND s.excluido = false",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoesNaoFinalizadasPorFuncionario(Pageable page, Long id);
	
	
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
	
	public int countByFuncionarioIdAndStatusAndExcluido(Long id, Status status, Boolean excluido);
	
	public int countByClienteIdAndExcluidoAndDataFinalizadoAfter(Long id, Boolean excluido, LocalDateTime dataFinalizado);
	
	public int countByFuncionarioIdAndExcluidoAndDataFinalizadoAfter(Long id, Boolean excluido, LocalDateTime dataFinalizado);
	
	public List<PojecaoResumidaFinalizados> findByClienteIdAndExcluidoAndStatusAndDataFinalizadoAfter(Long id, Boolean excluido, Status status, LocalDateTime dataFechamento);
	
	public List<PojecaoResumidaFinalizados> findByFuncionarioIdAndExcluidoAndStatusAndDataFinalizadoAfter(Long id, Boolean excluido, Status status, LocalDateTime dataFechamento);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO'AND s.cliente_id = :id AND s.local = :local AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorLocalPorCliente(Long id, String local, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO'AND s.funcionario_id = :id AND s.local = :local AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorLocalPorFuncionario(Long id, String local, Boolean excluido);
	//##################################################################################################################
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.funcionario_id = :id "
					+ "AND s.local = :local "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorLocalPorFuncionarioPeridoDataAbertura(Long id, String local, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.funcionario_id = :id "
					+ "AND s.formaAbertura = :formaAbertura "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido ")
	public int totalPorFormaAberturaPorFuncionarioPeriodoDataAbertura(Long id, String formaAbertura, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.funcionario_id = :id "
					+ "AND s.classificacao = :classificacao "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorClassificacaoPorFuncionarioPeriodoDataAbertura(Long id, String classificacao, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.funcionario_id = :id "
					+ "AND s.prioridade = :prioridade "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorPrioridadePorFuncionarioPeriodoDataAbertura(Long id, String prioridade, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.funcionario_id = :id "
					+ "AND s.status = :status "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorStatusPorFuncionarioPeriodoDataAbertura(Long id, String status, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.duracao FROM solicitacoes s "
					+ "WHERE s.funcionario_id = :id "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public List<PojecaoResumidaFinalizados> listaSolicitacoesPorFuncionarioPorPeriodoAbertura(Long id, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.funcionario_id = :id "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorFuncionarioPeriodoDataAbertura(Long id, Boolean excluido,  LocalDateTime dataInicio, LocalDateTime dataFim);
	
	//##################################################################################################################
	//##################################################################################################################
	
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.funcionario_id = :id "
						+ "AND s.local = :local "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorLocalPorFuncionarioPeridoDataFinalizado(Long id, String local, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.funcionario_id = :id "
						+ "AND s.formaAbertura = :formaAbertura "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido ")
		public int totalPorFormaAberturaPorFuncionarioPeriodoDataFinalizado(Long id, String formaAbertura, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.funcionario_id = :id "
						+ "AND s.classificacao = :classificacao "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorClassificacaoPorFuncionarioPeriodoDataFinalizado(Long id, String classificacao, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.funcionario_id = :id "
						+ "AND s.prioridade = :prioridade "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorPrioridadePorFuncionarioPeriodoDataFinalizado(Long id, String prioridade, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.funcionario_id = :id "
						+ "AND s.status = :status "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorStatusPorFuncionarioPeriodoDataFinalizado(Long id, String status, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		@Query(nativeQuery = true,
				value = "SELECT s.duracao FROM solicitacoes s "
						+ "WHERE s.funcionario_id = :id "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public List<PojecaoResumidaFinalizados> listaSolicitacoesPorFuncionarioPorPeriodoFinalizado(Long id, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.funcionario_id = :id "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorFuncionarioPeriodoDataFinalizado(Long id, Boolean excluido,  LocalDateTime dataInicio, LocalDateTime dataFim);
		
		//##################################################################################################################
		
		//##################################################################################################################
		
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.funcionario_id = :id "
							+ "AND s.local = :local "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorLocalPorFuncionarioPeridoDataAtualizado(Long id, String local, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.funcionario_id = :id "
							+ "AND s.formaAbertura = :formaAbertura "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido ")
			public int totalPorFormaAberturaPorFuncionarioPeriodoDataAtualizado(Long id, String formaAbertura, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.funcionario_id = :id "
							+ "AND s.classificacao = :classificacao "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorClassificacaoPorFuncionarioPeriodoDataAtualizado(Long id, String classificacao, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.funcionario_id = :id "
							+ "AND s.prioridade = :prioridade "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorPrioridadePorFuncionarioPeriodoDataAtualizado(Long id, String prioridade, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.funcionario_id = :id "
							+ "AND s.status = :status "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorStatusPorFuncionarioPeriodoDataAtualizado(Long id, String status, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			@Query(nativeQuery = true,
					value = "SELECT s.duracao FROM solicitacoes s "
							+ "WHERE s.funcionario_id = :id "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public List<PojecaoResumidaFinalizados> listaSolicitacoesPorFuncionarioPorPeriodoAtualizado(Long id, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.funcionario_id = :id "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorFuncionarioPeriodoDataAtualizado(Long id, Boolean excluido,  LocalDateTime dataInicio, LocalDateTime dataFim);
			
			//##################################################################################################################
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.cliente_id = :id AND s.formaAbertura = :formaAbertura and s.excluido = :excluido and s.status != 'FINALIZADO'", nativeQuery = true)
	public int totalPorFormaAberturaPorCliente(Long id, String formaAbertura, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.funcionario_id = :id AND s.formaAbertura = :formaAbertura and s.excluido = :excluido and s.status != 'FINALIZADO'", nativeQuery = true)
	public int totalPorFormaAberturaPorFuncionario(Long id, String formaAbertura, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO' AND s.cliente_id = :id AND s.classificacao = :classificacao AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorClassificacaoPorCliente(Long id, String classificacao, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO' AND s.funcionario_id = :id AND s.classificacao = :classificacao AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorClassificacaoPorFuncionario(Long id, String classificacao, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO' AND s.cliente_id = :id AND s.prioridade = :prioridade AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorPrioridadePorCliente(Long id, String prioridade, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.status != 'FINALIZADO' AND s.funcionario_id = :id AND s.prioridade = :prioridade AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorPrioridadePorFuncionario(Long id, String prioridade, Boolean excluido);
	
	@Query(value = "SELECT s.id, s.dataAtualizacao FROM solicitacoes s WHERE s.excluido = 'false' AND s.status != 'FINALIZADO' ORDER BY s.dataAtualizacao DESC LIMIT 1", nativeQuery = true)
	public DtoUltimaAtualizada buscaUltimaAtualizada();

	@Query(nativeQuery = true,
			value = "SELECT * FROM solicitacoes s WHERE s.excluido = 'false' AND s.status = 'ANDAMENTO'")
	public List<Solicitacao> buscaSolicitacoesEmAndamento();
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM solicitacoes s WHERE s.excluido = 'false' AND s.status = 'ABERTO'")
	public List<Solicitacao> buscaSolicitacoesAbertas();
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM solicitacoes s WHERE s.excluido = 'false' AND s.status = 'AGENDADO'")
	public List<Solicitacao> buscaSolicitacoesAgendadas();
	
	@Query(nativeQuery = true,
			value = "SELECT * FROM solicitacoes s WHERE s.excluido = 'false' AND s.status = 'PAUSADO' OR s.status = 'AGUARDANDO'")
	public List<Solicitacao> buscaSolicitacoesPausadoAguardando();

	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, s.dataFinalizado, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, "
			+ "s.duracao, s.dataAtualizacao, s.dataAgendado "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.id = :id "
			+ "AND s.excluido = false",nativeQuery = true)
	public ProjecaoDadosImpressao impressaoPorId(Long id);

	//##################################################################################################################
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.cliente_id = :id "
					+ "AND s.local = :local "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorLocalPorClientePeridoDataAbertura(Long id, String local, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.cliente_id = :id "
					+ "AND s.formaAbertura = :formaAbertura "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido ")
	public int totalPorFormaAberturaPorClientePeriodoDataAbertura(Long id, String formaAbertura, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.cliente_id = :id "
					+ "AND s.classificacao = :classificacao "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorClassificacaoPorClientePeriodoDataAbertura(Long id, String classificacao, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.cliente_id = :id "
					+ "AND s.prioridade = :prioridade "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorPrioridadePorClientePeriodoDataAbertura(Long id, String prioridade, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.cliente_id = :id "
					+ "AND s.status = :status "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorStatusPorClientePeriodoDataAbertura(Long id, String status, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	@Query(nativeQuery = true,
			value = "SELECT s.duracao FROM solicitacoes s "
					+ "WHERE s.cliente_id = :id "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public List<PojecaoResumidaFinalizados> listaSolicitacoesPorClientePorPeriodoAbertura(Long id, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
	
	@Query(nativeQuery = true,
			value = "SELECT COUNT(*) FROM solicitacoes s "
					+ "WHERE s.cliente_id = :id "
					+ "AND s.dataAbertura >= :dataInicio "
					+ "AND s.dataAbertura <= :dataFim "
					+ "AND s.excluido = :excluido")
	public int totalPorClientePeriodoDataAbertura(Long id, Boolean excluido,  LocalDateTime dataInicio, LocalDateTime dataFim);
	
	//##################################################################################################################
	//##################################################################################################################
	
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.cliente_id = :id "
						+ "AND s.local = :local "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorLocalPorClientePeridoDataFinalizado(Long id, String local, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.cliente_id = :id "
						+ "AND s.formaAbertura = :formaAbertura "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido ")
		public int totalPorFormaAberturaPorClientePeriodoDataFinalizado(Long id, String formaAbertura, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.cliente_id = :id "
						+ "AND s.classificacao = :classificacao "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorClassificacaoPorClientePeriodoDataFinalizado(Long id, String classificacao, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.cliente_id = :id "
						+ "AND s.prioridade = :prioridade "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorPrioridadePorClientePeriodoDataFinalizado(Long id, String prioridade, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.cliente_id = :id "
						+ "AND s.status = :status "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorStatusPorClientePeriodoDataFinalizado(Long id, String status, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		@Query(nativeQuery = true,
				value = "SELECT s.duracao FROM solicitacoes s "
						+ "WHERE s.cliente_id = :id "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public List<PojecaoResumidaFinalizados> listaSolicitacoesPorClientePorPeriodoFinalizado(Long id, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
		
		@Query(nativeQuery = true,
				value = "SELECT COUNT(*) FROM solicitacoes s "
						+ "WHERE s.cliente_id = :id "
						+ "AND s.dataFinalizado >= :dataInicio "
						+ "AND s.dataFinalizado <= :dataFim "
						+ "AND s.excluido = :excluido")
		public int totalPorClientePeriodoDataFinalizado(Long id, Boolean excluido,  LocalDateTime dataInicio, LocalDateTime dataFim);
		
		//##################################################################################################################
		
		//##################################################################################################################
		
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.cliente_id = :id "
							+ "AND s.local = :local "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorLocalPorClientePeridoDataAtualizado(Long id, String local, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.cliente_id = :id "
							+ "AND s.formaAbertura = :formaAbertura "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido ")
			public int totalPorFormaAberturaPorClientePeriodoDataAtualizado(Long id, String formaAbertura, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.cliente_id = :id "
							+ "AND s.classificacao = :classificacao "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorClassificacaoPorClientePeriodoDataAtualizado(Long id, String classificacao, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.cliente_id = :id "
							+ "AND s.prioridade = :prioridade "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorPrioridadePorClientePeriodoDataAtualizado(Long id, String prioridade, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.cliente_id = :id "
							+ "AND s.status = :status "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorStatusPorClientePeriodoDataAtualizado(Long id, String status, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			@Query(nativeQuery = true,
					value = "SELECT s.duracao FROM solicitacoes s "
							+ "WHERE s.cliente_id = :id "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public List<PojecaoResumidaFinalizados> listaSolicitacoesPorClientePorPeriodoAtualizado(Long id, Boolean excluido, LocalDateTime dataInicio, LocalDateTime dataFim);
			
			@Query(nativeQuery = true,
					value = "SELECT COUNT(*) FROM solicitacoes s "
							+ "WHERE s.cliente_id = :id "
							+ "AND s.dataAtualizacao >= :dataInicio "
							+ "AND s.dataAtualizacao <= :dataFim "
							+ "AND s.excluido = :excluido")
			public int totalPorClientePeriodoDataAtualizado(Long id, Boolean excluido,  LocalDateTime dataInicio, LocalDateTime dataFim);

			public Solicitacao findByDescricaoAndStatus(String string, Status aberto);
			
			//##################################################################################################################

}
