package br.com.techgol.app.model;

import java.util.Date;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.model.enums.Funcao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Funcionario extends Usuario {
	
	@NotBlank
	@Column(length = 50)
	private String nomeFuncionario;
	@Enumerated(EnumType.STRING)
	private Funcao funcao;
	private String caminhoFoto;
	
	public Funcionario(DtoCadastroFuncionario dados) {
		
		this.nomeFuncionario = dados.nomeFuncionario();
		this.funcao = dados.funcao();
		this.setUsuario(dados.usuario());
		this.setSenha(dados.senha());
		this.setAtivo(true);
		this.setMfa(false);
		this.setDataAtualizacao(new Date());
		
	}

}
