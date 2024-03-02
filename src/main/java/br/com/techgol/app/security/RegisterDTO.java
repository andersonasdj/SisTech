package br.com.techgol.app.security;

public record RegisterDTO(
		String username,
		String password,
		String role
		) {

}
