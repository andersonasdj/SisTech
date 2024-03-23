package br.com.techgol.app.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.LogLogin;
import br.com.techgol.app.services.FuncionarioService;
import br.com.techgol.app.services.LogLoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private LogLoginService loginService;
	
//	@Autowired
//	SessionRegistry registry;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if(request.getRequestURI().trim().equals("/sistech/login") && request.getMethod().trim().equals("POST")) {
			doFilter(request, response, filterChain);
			
			if(SecurityContextHolder.getContext().getAuthentication() != null) {
				Funcionario funcionario = funcionarioService.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
				
				if(funcionario.getAtivo()) {
					System.out.println("\nDENTRO DO FILTRO!");
					System.out.println("FUNCIONARIO LOGADO: " + funcionario.getNomeFuncionario());
					System.out.println("LOCAL: " + request.getLocalName());
					System.out.println("ENDERECO IP: "+ request.getLocalAddr());
					System.out.println("PAIS: " +response.getLocale().getCountry());
					System.out.println("ENDEREÇO REMOTO: " + request.getRemoteAddr());
					System.out.println("HOST REMOTO: " + request.getRemoteHost());
					System.out.println("ROLE: " + funcionario.getRole());
					System.out.println("ATIVO : "+funcionario.getAtivo());
					System.out.println("URI: " + request.getRequestURI()+"\n");
					
					System.out.println("getPathTranslated "+request.getHeaderNames().toString());
					
					funcionarioService.atualizaIpLogin(funcionario, request.getRemoteHost(),response.getLocale().getCountry());
					loginService.salvaLog(new LogLogin(
							LocalDateTime.now().withNano(0),
							request.getLocalAddr(),
							response.getLocale().getCountry(),
							request.getRemoteHost(),
							funcionario.getNomeFuncionario(),
							request.getRemoteAddr(),
							request.getLocalName(),
							request.getRequestURI()
							));
					
				}else {
					System.out.println("USUÁRIO DESATIVADO");
					SecurityContextHolder.getContext().setAuthentication(null);
				}
					
			}else {
				System.out.println("USUARIO SEM SESSÃO PASSOU NO FILTRO!");
				SecurityContextHolder.clearContext();
			}
		}
			
			
			
			
			
		
		
		
//		if(SecurityContextHolder.getContext().getAuthentication() != null) {
//			Funcionario funcionarioBase = service.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
//			
//			if(request.getRequestURI().equals("/sistech/login") && request.getMethod().equals("POST")) {
//				System.out.println("\nDENTRO DO FILTRO!");
//				System.out.println("FUNCIONARIO LOGADO: " + funcionarioBase.getNomeFuncionario());
//				System.out.println("LOCAL: " + request.getLocalName());
//				System.out.println("ENDERECO IP: "+ request.getLocalAddr());
//				System.out.println("PAIS: " +response.getLocale().getCountry());
//				System.out.println("ENDEREÇO REMOTO: " + request.getRemoteAddr());
//				System.out.println("HOST REMOTO: " + request.getRemoteHost());
//				//System.out.println(funcionarioBase.getRole());
//				System.out.println("Status ativo : "+funcionarioBase.getAtivo());
//				System.out.println("URI: " + request.getRequestURI()+"\n");
//				service.atualizaIpLogin(funcionarioBase, request.getRemoteHost(),response.getLocale().getCountry());
//			}
//			
//			if(!funcionarioBase.getAtivo()) {
//				SecurityContextHolder.clearContext();
//			}
//			
//		}else {
//			System.out.println("USUARIO SEM SESSÃO PASSOU NO FILTRO!");
//			//SecurityContextHolder.clearContext();
//		}
		
		filterChain.doFilter(request, response);
	
		
//		List<Object> allPrincipal = registry.getAllPrincipals();
//		System.out.println("SESSÕES ATIVA: " +allPrincipal.size());
		
	}

}
