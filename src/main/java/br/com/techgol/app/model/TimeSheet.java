package br.com.techgol.app.model;

import java.time.LocalDateTime;

import br.com.techgol.app.model.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "timesheet")
public class TimeSheet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Solicitacao solicitacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Funcionario funcionario;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime inicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime fim;
	
	private Long duracao;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	public TimeSheet(Solicitacao solicitacao, Funcionario funcionario, LocalDateTime inicio, LocalDateTime fim, Long duração, Status status) {
		this.solicitacao = solicitacao;
		this.funcionario = funcionario;
		this.inicio = inicio;
		this.fim = fim;
		this.duracao = duração;
		this.status = status;
	}
}
