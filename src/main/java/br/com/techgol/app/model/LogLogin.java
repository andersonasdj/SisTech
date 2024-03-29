package br.com.techgol.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "logLogin")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogLogin {
	
	public LogLogin(LocalDateTime dataLogin, String localAddr, String country, String remoteHost, String nomeFuncionario,
			String remoteAddr, String localName, String requestURI, String browser, String status, String descricao) {
	
		this.dataLogin=dataLogin;
		this.enderecoServer=localAddr;
		this.pais=country;
		this.hostname=remoteHost;
		this.usuario=nomeFuncionario;
		this.ip=remoteAddr;
		this.nomeLocal=localName;
		this.uri=requestURI;
		this.browser = browser;
		this.status = status;
		this.descricao = descricao;
		
	
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dataLogin;
	
	@Column(length = 60)
	private String ip;
	
	@Column(length = 20)
	private String pais;
	
	@Column(length = 60)
	private String hostname;
	
	@Column(length = 300)
	private String usuario;
	
	@Column(length = 60)
	private String nomeLocal;
	
	@Column(length = 100)
	private String enderecoServer;
	
	@Column(length = 100)
	private String uri;
	
	@Column(length = 250)
	private String browser;
	
	@Column(length = 20)
	private String status;
	
	@Column(length = 60)
	private String descricao;
	
}
