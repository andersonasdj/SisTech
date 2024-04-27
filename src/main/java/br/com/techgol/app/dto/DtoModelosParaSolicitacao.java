package br.com.techgol.app.dto;

import java.util.List;

public record DtoModelosParaSolicitacao(
	
	List<String> modelos,
	List<String> modelosId){


	public DtoModelosParaSolicitacao(List<String> modelos, List<String> modelosId) {
		
		this.modelos = modelos .stream().map(String::new).toList();
		this.modelosId = modelosId.stream().map(String::new).toList();
	}

}

