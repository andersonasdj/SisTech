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

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{
	
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura, s.dataAtualizacao "
			+ "FROM solicitacoes s "
			+ "INNER JOIN clientes c ON s.cliente_id=c.id "
			+ "LEFT JOIN funcionarios f ON s.funcionario_id=f.id "
			+ "WHERE s.status != :status " 
			+ "AND s.excluido = :excluida",nativeQuery = true)
	public Page<SolicitacaoProjecao> listarSolicitacoes(Pageable page, String status, Boolean excluida);
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, s.duracao, "
			+ "s.local, s.observacao, s.prioridade, s.resolucao, c.vip, "
			+ "s.solicitante, s.status, c.nomeCliente, f.nomeFuncionario, s.dataAbertura,  s.dataAtualizacao "
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
	
	
	@Query(value = "SELECT s.id, s.abertoPor, s.afetado, s.categoria, "
			+ "s.classificacao, s.descricao, s.formaAbertura, "
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
	
	
	public int countByStatusAndExcluido(Status status, Boolean excluido);
	

	public Long countByStatusAndFuncionarioId(Status status, Long id);

	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.local = :local and s.excluido = :excluido and s.status != :status", nativeQuery = true)
	public int totalPorLocal(String local, Boolean excluido, String status);
	
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
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.cliente_id = :id AND s.local = :local AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorLocalPorCliente(Long id, String local, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.cliente_id = :id AND s.classificacao = :classificacao AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorClassificacaoPorCliente(Long id, String classificacao, Boolean excluido);
	
	@Query(value = "SELECT COUNT(*) FROM solicitacoes s WHERE s.cliente_id = :id AND s.prioridade = :prioridade AND s.excluido = :excluido", nativeQuery = true)
	public int totalPorPrioridadePorCliente(Long id, String prioridade, Boolean excluido);

	
	@Query(value = "SELECT s.id, s.dataAtualizacao FROM solicitacoes s WHERE s.excluido = 'false' AND s.status != 'FINALIZADO' ORDER BY s.dataAtualizacao DESC LIMIT 1", nativeQuery = true)
	public DtoUltimaAtualizada buscaUltimaAtualizada();
	

}
