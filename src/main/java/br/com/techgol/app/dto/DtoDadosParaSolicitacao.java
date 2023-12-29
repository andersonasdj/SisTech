package br.com.techgol.app.dto;

import java.util.List;

public record DtoDadosParaSolicitacao(
		
		List<String> clientes,
		List<String> funcionarios){
	
	
		public DtoDadosParaSolicitacao(List<String> clientes, List<String> funcionarios) {
			
			this.clientes = clientes .stream().map(String::new).toList();
			this.funcionarios = funcionarios.stream().map(String::new).toList();
			
		}

}
