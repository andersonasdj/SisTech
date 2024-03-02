package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.techgol.app.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	
	@Query(value = "SELECT f.nomeFuncionario FROM funcionarios f  WHERE f.ativo=true ORDER BY f.nomeFuncionario", nativeQuery = true)
	public List<String> listarNomesFuncionarios();
	
	@Query(value = "SELECT f.id FROM funcionarios f WHERE f.ativo=true ORDER BY f.nomeFuncionario", nativeQuery = true)
	public List<String> listarIdFuncionarios();
	
	
	public Funcionario findBynomeFuncionario(String nomeFuncionario);
	
	
	@Query(value = "SELECT * FROM funcionarios f  WHERE f.username=:username", nativeQuery = true)
	public Funcionario buscarPorUsername(String username);
	
	
	public UserDetails findByUsername(String username);

	public Boolean existsByUsername(String username);
	
	public Boolean existsByNomeFuncionario(String nomeFuncionario);
}
