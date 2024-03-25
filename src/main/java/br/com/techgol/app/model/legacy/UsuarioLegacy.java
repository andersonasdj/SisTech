package br.com.techgol.app.model.legacy;

import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@MappedSuperclass
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UsuarioLegacy {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String usuario;
	private String senha;
	private int mfa;
	private String email;
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtualizacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimoLogin;
	private boolean statusMfa;
	private String ip;
	
}
	