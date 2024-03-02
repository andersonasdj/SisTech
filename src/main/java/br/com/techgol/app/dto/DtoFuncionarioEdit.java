package br.com.techgol.app.dto;

import java.util.Date;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.UserRole;
import br.com.techgol.app.model.enums.Funcao;

public record DtoFuncionarioEdit(
		Long id,
		String nomeFuncionario,
		String username,
		String password,
		Funcao funcao,
		UserRole role,
		Boolean ativo,
		Boolean mfa,
		Date dataAtualizacao,
		Date dataUltimoLogin
		) {
	
	public DtoFuncionarioEdit(Funcionario f) {
		this(
				f.getId(), 
				f.getNomeFuncionario(), 
				f.getUsername(), 
				f.getPassword(), 
				f.getFuncao(),
				f.getRole(),
				f.getAtivo(),
				f.getMfa(),
				f.getDataAtualizacao(),
				f.getDataUltimoLogin());
	}

}
