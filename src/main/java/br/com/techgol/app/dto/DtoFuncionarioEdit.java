package br.com.techgol.app.dto;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.UserRole;

public record DtoFuncionarioEdit(
		Long id,
		String nomeFuncionario,
		String username,
		String password,
		UserRole role,
		Boolean ativo,
		Boolean mfa,
		String dataAtualizacao
		) {
	
	public DtoFuncionarioEdit(Funcionario f) {
		this(
				f.getId(), 
				f.getNomeFuncionario(), 
				f.getUsername(), 
				f.getPassword(), 
				f.getRole(),
				f.getAtivo(),
				f.getMfa(),
				f.getDataAtualizacao().toString()
				);
	}

}
