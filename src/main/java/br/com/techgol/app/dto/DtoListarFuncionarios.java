package br.com.techgol.app.dto;

import br.com.techgol.app.model.Funcionario;

public record DtoListarFuncionarios(
		
		Long id,
		String nomeFuncionario,
		String username,
		Boolean ativo,
		Boolean mfa,
		String role,
		String dataAtualizacao
		) {
	
	
	public DtoListarFuncionarios(Funcionario f) {
		this(f.getId(), f.getNomeFuncionario(), f.getUsername(), f.getAtivo(), f.getMfa(), f.getRole().toString(), f.getDataAtualizacao().toString());
	}
	

}
 