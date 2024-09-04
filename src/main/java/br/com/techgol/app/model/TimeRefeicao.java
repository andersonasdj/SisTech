package br.com.techgol.app.model;

import java.time.LocalDateTime;

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

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "timerefeicao")
public class TimeRefeicao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Funcionario funcionario;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime inicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime fim;
	
	private Long duracao;
	
	public TimeRefeicao(Funcionario funcionario, LocalDateTime inicio, LocalDateTime fim, Long duração) {
		this.funcionario = funcionario;
		this.inicio = inicio;
		this.fim = fim;
		this.duracao = duração;
	}

	public TimeRefeicao(LocalDateTime inicio, Funcionario funcionario) {
		this.inicio=inicio;
		this.setFuncionario(funcionario);
		
	}
}
