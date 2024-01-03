package br.com.techgol.app.dto;

import java.util.Date;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.enums.Funcao;

public record DtoFuncionarioEdit(
		Long id,
		String nomeFuncionario,
		String usuario,
		String senha,
		Funcao funcao,
		Boolean ativo,
		Boolean mfa,
		Date dataAtualizacao,
		Date dataUltimoLogin
		) {
	
	public DtoFuncionarioEdit(Funcionario f) {
		this(
				f.getId(), 
				f.getNomeFuncionario(), 
				f.getUsuario(), 
				f.getSenha(), 
				f.getFuncao(),
				f.getAtivo(),
				f.getMfa(),
				f.getDataAtualizacao(),
				f.getDataUltimoLogin());
	}

}
