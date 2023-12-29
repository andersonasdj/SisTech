package br.com.techgol.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.techgol.app.model.Colaborador;
import br.com.techgol.app.orm.ColaboradorProjecao;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long>{

	@Query(value = "SELECT co.id, co.nomeColaborador, co.celular, co.vip, co.cliente_id FROM colaboradores co "
			+ "WHERE co.cliente_id=:id ", nativeQuery = true)
	List<ColaboradorProjecao> buscaColaboradoresPorIdCliente(Long id);

}
