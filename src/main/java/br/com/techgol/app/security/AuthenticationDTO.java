package br.com.techgol.app.security;

public record AuthenticationDTO(
		String username, 
		String password) {
}
