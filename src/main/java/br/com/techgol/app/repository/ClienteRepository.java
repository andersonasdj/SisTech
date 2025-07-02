package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	@Query(value = "SELECT * FROM clientes c WHERE c.ativo=true", nativeQuery = true)
	public List<Cliente> listarClientes();
	
	@Query(value = "SELECT * "
			+ "FROM clientes c "
			+ "WHERE c.nomeCliente COLLATE utf8mb4_general_ci LIKE CONCAT('%', :conteudo, '%') "
			+ "ORDER BY c.id DESC" ,nativeQuery = true)
	public Page<Cliente> listarClientesPorPalavra(Pageable page, String conteudo);
	
	@Query(value = "SELECT c.nomeCliente FROM clientes c ORDER BY c.nomeCliente", nativeQuery = true)
	public List<String> listarNomesCliente();
	
	@Query(value = "SELECT c.id FROM clientes c ORDER BY c.nomeCliente", nativeQuery = true)
	public List<String> listarIdCliente();
	
	@Query(value = "SELECT c.nomeCliente FROM clientes c WHERE c.ativo=1 ORDER BY c.nomeCliente", nativeQuery = true)
	public List<String> listarNomesClienteAtivos();
	
	@Query(value = "SELECT c.id FROM clientes c WHERE c.ativo=1 ORDER BY c.nomeCliente", nativeQuery = true)
	public List<String> listarIdClienteAtivos();
	
	public Cliente findBynomeCliente(String nomeCliente);

	@Query(value = "SELECT c.vip FROM clientes c WHERE c.id= :id", nativeQuery = true)
	public boolean verificaSeVip(Long id);
	
	@Query(value = "SELECT c.redFlag FROM clientes c WHERE c.id= :id", nativeQuery = true)
	public boolean verificaSeRedFlag(Long id);
	
	@Query(value = "SELECT c.bairro FROM clientes c WHERE c.ativo=1 AND c.bairro != 'null' ORDER BY c.bairro", nativeQuery = true)
	public List<String> listarBairrosClientes();
	
	@Query(value = "SELECT * FROM clientes c WHERE c.ativo=1 AND c.bairro = :bairro ORDER BY c.nomeCliente", nativeQuery = true)
	public List<Cliente> listarClientesPorBairro(String bairro);
	
	@Query(value = "SELECT * FROM clientes c WHERE c.ativo=1 AND c.vip = 1 ORDER BY c.nomeCliente", nativeQuery = true)
	public List<Cliente> listarClientesVip();
	
	@Query(value = "SELECT * FROM clientes c WHERE c.ativo=1 AND c.redFlag = 1 ORDER BY c.nomeCliente", nativeQuery = true)
	public List<Cliente> listarClientesRedFlag();
	
	@Query(value = "SELECT * FROM clientes c WHERE c.ativo=1 AND c.redFlag = 1 AND c.vip = 1 ORDER BY c.nomeCliente", nativeQuery = true)
	public List<Cliente> listarClientesRedFlagEVip();

	public Cliente findByToken(String token);
}
