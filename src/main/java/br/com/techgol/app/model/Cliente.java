package br.com.techgol.app.model;

import java.time.LocalDateTime;

import br.com.techgol.app.dto.DtoAtualizarCliente;
import br.com.techgol.app.dto.DtoCadastroCliente;
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
	
	
	@Column(length = 100)
	private String nomeCliente;
	@Column(length = 200)
	private String endereco;
	@Column(length = 20)
	private String telefone;
	@Column(length = 20)
	private String cnpj;
	private boolean redFlag;
	private boolean vip;
	
	public Cliente(DtoCadastroCliente dados) {
		
		this.nomeCliente = dados.nomeCliente();
		this.setUsername(dados.username());
		this.setPassword(dados.password());
		this.setAtivo(true);
		this.setMfa(false);
		this.setDataAtualizacao(LocalDateTime.now().withNano(0));
		this.endereco = dados.endereco();
		this.telefone = dados.telefone();
		this.cnpj = dados.cnpj();
		this.redFlag = false;
		this.vip = false;
	}
	
public Cliente(DtoAtualizarCliente dados) {
		
		this.setId(dados.id());
		this.nomeCliente = dados.nomeCliente();
		this.setUsername(dados.username());
		this.setPassword(dados.password());
		this.setAtivo(dados.ativo());
		this.setMfa(false);
		this.setDataAtualizacao(LocalDateTime.now().withNano(0));
		this.endereco = dados.endereco();
		this.telefone = dados.telefone();
		this.cnpj = dados.cnpj();
		this.redFlag = dados.redFlag();
		this.vip = dados.vip();
	}
}
