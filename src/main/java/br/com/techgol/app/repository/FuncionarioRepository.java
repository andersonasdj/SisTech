package br.com.techgol.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgol.app.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

}
