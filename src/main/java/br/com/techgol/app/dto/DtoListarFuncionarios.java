package br.com.techgol.app.dto;

import java.io.Serializable;

import br.com.techgol.app.model.Funcionario;

public record DtoListarFuncionarios(
		
		Long id,
		String nomeFuncionario,
		String username,
		Boolean ativo,
		Boolean mfa,
		Boolean ausente,
		Boolean refeicao,
		String role,
		String dataAtualizacao
		) implements Serializable {
	
	
	public DtoListarFuncionarios(Funcionario f) {
		this(f.getId(), f.getNomeFuncionario(), f.getUsername(), f.getAtivo(), f.getMfa(), f.getAusente(), f.getRefeicao(), f.getRole().toString(), f.getDataAtualizacao().toString());
	}
	

}
 