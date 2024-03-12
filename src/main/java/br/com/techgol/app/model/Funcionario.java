package br.com.techgol.app.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.model.enums.Funcao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Funcionario extends Usuario implements UserDetails {
	
	private static final long serialVersionUID = 3474835326939061879L;
	@Column(length = 50)
	private String nomeFuncionario;
	@Enumerated(EnumType.STRING)
	private Funcao funcao;
	private String caminhoFoto;
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	public Funcionario(DtoCadastroFuncionario dados) {
		
		String senhaEncriptada = new BCryptPasswordEncoder().encode(dados.password());
		
		this.nomeFuncionario = dados.nomeFuncionario();
		this.setRole(dados.role());
		this.setUsername(dados.username());
		this.setPassword(senhaEncriptada);
		this.setAtivo(true);
		this.setMfa(false);
		this.setDataAtualizacao(LocalDateTime.now());
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER") );
		else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	 
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
