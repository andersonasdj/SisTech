package br.com.techgol.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgol.app.model.LogSolicitacao;

public interface LogSolicitacaoRepository extends JpaRepository<LogSolicitacao, Long> {

}
