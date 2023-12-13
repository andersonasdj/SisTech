package br.com.techgol.app.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(length = 20)
	private String usuario;
	@NotBlank
	@Column(length = 50)
	private String senha;
	@Column(length = 100)
	private String ip;
	private Boolean ativo;
	private Boolean mfa;
	private Date dataAtualizacao;
	private Date dataUltimoLogin;

}
