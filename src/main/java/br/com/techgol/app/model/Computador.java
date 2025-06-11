package br.com.techgol.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "computadorSolicitacao.java")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Computador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime last_seen;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime agent_install_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime last_boot_time;
	
	@Column(name = "type", length = 60)
	private String type;
	
	@Column(name="status", length = 60)
	private String status;
	
	@Column(name="name", length = 300)
	private String name;
	
	@Column(name="address", length = 300)
	private String address;
	
	@Column(name="os", length = 300)
	private String os;
	
	@Column(name="platform", length = 60)
	private String platform;
	
	@Column(name="comment", length = 60)
	private String comment;
	
	@Column(name="mac", length = 60)
	private String mac;
	
	@Column(name="serial", length = 60)
	private String serial;
	
	@Column(name="device_name", length = 60)
	private String device_name;
	
	@Column(name="cpu_name", length = 200)
	private String cpu;
	
	@Column(name="disk", length = 200)
	private String disk;
	
	@Column(name="manufacturer", length = 200)
	private String manufacturer;
	
	@Column(name="ram", length = 200)
	private String ram;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Funcionario funcionario;


}
