package br.com.techgol.app.model;

import java.util.Collection;
import java.util.Date;
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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Funcionario extends Usuario implements UserDetails {
	
	@Column(length = 50)
	private String nomeFuncionario;
	@Enumerated(EnumType.STRING)
	private Funcao funcao;
	private String caminhoFoto;
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	public Funcionario(DtoCadastroFuncionario dados) {
		
		String senhaEncriptada = new BCryptPasswordEncoder().encode(dados.senha());
		
		this.nomeFuncionario = dados.nomeFuncionario();
		this.setRole(dados.role());
		this.setUsuario(dados.usuario());
		this.setSenha(senhaEncriptada);
		this.setAtivo(true);
		this.setMfa(false);
		this.setDataAtualizacao(new Date());
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER") );
		else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	 
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return getUsuario();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
