package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.techgol.app.model.Colaborador;
import br.com.techgol.app.orm.ColaboradorProjecao;
import br.com.techgol.app.orm.ColaboradorProjecaoSimples;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long>{
	
	@Query(value = "SELECT co.email FROM colaboradores co "
			+ "WHERE co.cliente_id=:id "
			+ "AND co.nomeColaborador = :nome "
			+ "LIMIT 1", nativeQuery = true)
	String retornarEmailColaboradorPorIdClienteNome(Long id, String nome);
	

	@Query(value = "SELECT co.id, co.nomeColaborador, co.celular, co.vip, co.cliente_id FROM colaboradores co "
			+ "WHERE co.cliente_id=:id ORDER BY co.nomeColaborador", nativeQuery = true)
	List<ColaboradorProjecao> buscaColaboradoresPorIdCliente(Long id);
	
	@Query(value = "SELECT co.nomeColaborador FROM colaboradores co "
			+ "WHERE co.cliente_id=:id ORDER BY co.nomeColaborador", nativeQuery = true)
	List<String> listarNomesColaboradoresPorIdCliente(Long id);
	
	
	@Query(value = "SELECT co.nomeColaborador, co.celular FROM colaboradores co "
			+ "WHERE co.cliente_id=:id ORDER BY co.nomeColaborador", nativeQuery = true)
	List<ColaboradorProjecaoSimples> listarNomesCelularColaboradoresPorIdCliente(Long id);
	
	@Query(value = "SELECT co.celular, co.vip FROM colaboradores co "
			+ "WHERE co.cliente_id=:id AND co.nomeColaborador=:nomeColaborador", nativeQuery = true)
	String listarCelularColaborador(Long id, String nomeColaborador);
	
	
	@Query(value = "SELECT * FROM colaboradores co WHERE co.id=:id", nativeQuery = true)
	public Colaborador buscaPorId(Long id);
	
	
	@Query(value = "SELECT COUNT(*) FROM colaboradores co WHERE co.cliente_id=:cliente_id AND co.nomeColaborador=:nomeColaborador", nativeQuery = true)
	public int verificaSeExistePorId(Long cliente_id, String nomeColaborador);
	
	@Query(value = "SELECT COUNT(*) FROM colaboradores co WHERE co.nomeColaborador=:nomeColaborador AND co.email=:email", nativeQuery = true)
	public int verificaSeExistePorNome(String nomeColaborador, String email);


	@Query("""
	        SELECT 
	            c.id            AS id,
	            c.nomeColaborador AS nomeColaborador,
	            c.celular        AS celular,
	            c.vip            AS vip,
	            c.cliente.id     AS cliente_id,
	            c.email          AS email,
	            c.cliente.nomeCliente	AS nomeCliente,
	            c.cliente.vip	AS vipCliente
	        FROM Colaborador c
	        WHERE 
		        LOWER(c.nomeColaborador) LIKE LOWER(CONCAT('%', :dados, '%'))
		        OR LOWER(c.email)        LIKE LOWER(CONCAT('%', :dados, '%'))
		        OR c.celular             LIKE CONCAT('%', :dados, '%')
		        OR LOWER(c.cliente.nomeCliente) LIKE LOWER(CONCAT('%', :dados, '%'))
	    """)
	    List<ColaboradorProjecao> buscarPorPalavraChave(@Param("dados") String dados);

}
