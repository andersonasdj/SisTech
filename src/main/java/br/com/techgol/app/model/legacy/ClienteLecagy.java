package br.com.techgol.app.model.legacy;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Cliente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteLecagy extends UsuarioLegacy {

	private String endereco;
	private String telefone1;
	private String cnpj;
	private String razaoSocial;
	private String responsavelTecnico;
	private double latitude;
	private double longitude;
	private boolean redFlag;
	private boolean vip;
	
}