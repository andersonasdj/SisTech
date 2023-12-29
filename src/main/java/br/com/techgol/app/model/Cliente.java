package br.com.techgol.app.model;

import java.util.Date;

import br.com.techgol.app.dto.DTOAtualizarCliente;
import br.com.techgol.app.dto.DTOCadastroCliente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends Usuario {
	
	
	@Column(length = 50)
	private String nomeCliente;
	@Column(length = 50)
	private String endereco;
	@Column(length = 20)
	private String telefone;
	@Column(length = 20)
	private String cnpj;
	private boolean redFlag;
	private boolean vip;
	
	public Cliente(DTOCadastroCliente dados) {
		
		this.nomeCliente = dados.nomeCliente();
		this.setUsuario(dados.usuario());
		this.setSenha(dados.senha());
		this.setAtivo(true);
		this.setMfa(false);
		this.setDataAtualizacao(new Date());
		this.endereco = dados.endereco();
		this.telefone = dados.telefone();
		this.cnpj = dados.cnpj();
		this.redFlag = false;
		this.vip = false;
	}
	
public Cliente(DTOAtualizarCliente dados) {
		
		this.setId(dados.id());
		this.nomeCliente = dados.nomeCliente();
		this.setUsuario(dados.usuario());
		this.setSenha(dados.senha());
		this.setAtivo(true);
		this.setMfa(false);
		this.setDataAtualizacao(new Date());
		this.endereco = dados.endereco();
		this.telefone = dados.telefone();
		this.cnpj = dados.cnpj();
		this.redFlag = false;
		this.vip = false;
	}
}
