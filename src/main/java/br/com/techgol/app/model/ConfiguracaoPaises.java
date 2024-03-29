package br.com.techgol.app.model;

import jakarta.persistence.Entity;
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
@Table(name = "configPaises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfiguracaoPaises {
	
	public ConfiguracaoPaises(String p, boolean s) {
		this.pais = p;
		this.status = s;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String pais;
	
	private boolean status;
	
	
}
