package br.com.techgol.app.dto;

import java.util.List;

public record DtoDadosParaSolicitacao(
		
		List<String> clientes,
		List<String> clientesId,
		List<String> funcionarios,
		List<String> funcionariosId,
		Boolean refeicao){
	
	
		public DtoDadosParaSolicitacao(List<String> clientes, List<String> clientesId, List<String> funcionarios, List<String> funcionariosId, Boolean refeicao) {
			
			this.clientes = clientes .stream().map(String::new).toList();
			this.clientesId = clientesId.stream().map(String::new).toList();
			this.funcionarios = funcionarios.stream().map(String::new).toList();
			this.funcionariosId = funcionariosId.stream().map(String::new).toList();
			this.refeicao = refeicao;
		}

}
