package br.com.techgol.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgol.app.model.Solicitacao;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>{

}
