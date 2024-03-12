package br.com.techgol.app.dto;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.enums.Funcao;

public record DtoListarFuncionarios(
		
		Long id,
		String nomeFuncionario,
		String username,
		Funcao funcao,
		Boolean ativo,
		Boolean mfa,
		String dataAtualizacao
		) {
	
	
	public DtoListarFuncionarios(Funcionario f) {
		this(f.getId(), f.getNomeFuncionario(), f.getUsername(), f.getFuncao(), f.getAtivo(), f.getMfa(), f.getDataAtualizacao().toString());
	}
	

}
 