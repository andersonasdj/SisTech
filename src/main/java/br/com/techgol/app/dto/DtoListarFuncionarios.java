package br.com.techgol.app.dto;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.enums.Funcao;
import br.com.techgol.app.util.ConverteDataToString;

public record DtoListarFuncionarios(
		
		Long id,
		String nomeFuncionario,
		String usuario,
		Funcao funcao,
		Boolean ativo,
		Boolean mfa,
		String dataAtualizacao,
		String dataUltimoLogin
		
		) {
	
	
	public DtoListarFuncionarios(Funcionario f) {
		this(f.getId(), f.getNomeFuncionario(), f.getUsuario(), f.getFuncao(), f.getAtivo(),
				f.getMfa(), new ConverteDataToString().convert(f.getDataAtualizacao()), 
				new ConverteDataToString().convert(f.getDataUltimoLogin()));
	}

}
 