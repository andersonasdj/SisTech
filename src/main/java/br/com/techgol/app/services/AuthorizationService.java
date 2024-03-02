package br.com.techgol.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.techgol.app.repository.FuncionarioRepository;

@Service
public class AuthorizationService implements UserDetailsService{
	
	@Autowired
	FuncionarioRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = repository.findByUsername(username);
		if(user == null) {
			throw new Error("User does not exists!");
		}
		return user;
	}

}
