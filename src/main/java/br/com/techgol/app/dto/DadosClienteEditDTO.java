package br.com.techgol.app.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.techgol.app.model.Cliente;

public record DadosClienteEditDTO(
		Long id,
		Boolean ativo,
		Boolean vip,
		Boolean redFlag,
		String nomeCliente,
		String endereco,
		String telefone,
		String username,
		String password,
		String cnpj,
		String bairro,
		Long tempoContratado,
		String token,
		String dominio,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime dataUltimoLogin
		) {
	
	public DadosClienteEditDTO(Cliente c) {
		this(c.getId(),
				c.getAtivo(),
				c.isVip(),
				c.isRedFlag(),
				c.getNomeCliente(),
				c.getEndereco(),
				c.getTelefone(),
				c.getUsername(),
				c.getPassword(),
				c.getCnpj(),
				c.getBairro(),
				c.getTempoContratado(),
				c.getToken(),
				c.getDominio(),
				c.getDataUltimoLogin());
		
	}
	

}
