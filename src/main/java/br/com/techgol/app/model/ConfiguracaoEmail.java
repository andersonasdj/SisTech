package br.com.techgol.app.model;

import br.com.techgol.app.dto.DtoEmails;
import br.com.techgol.app.model.enums.Agendamentos;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "configEmail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfiguracaoEmail {
	
	public ConfiguracaoEmail(String email, boolean status, Agendamentos agendamento) {
		this.email = email;
		this.agendamento = agendamento;
		this.status = status;
	}

	public ConfiguracaoEmail(DtoEmails d) {
		this.id=d.id();
		this.email=d.email();
		this.agendamento=d.agendamento();
		this.status=d.status();
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private Agendamentos agendamento;
	
	private String email;
	
	private boolean status;
	
	
}
