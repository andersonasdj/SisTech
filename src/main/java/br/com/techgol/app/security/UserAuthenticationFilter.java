package br.com.techgol.app.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.techgol.app.bruteforce.DefaultBruteForceProtectionService;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.LogLogin;
import br.com.techgol.app.services.ConfiguracaoPaisesService;
import br.com.techgol.app.services.FuncionarioService;
import br.com.techgol.app.services.LogLoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

	@Autowired private FuncionarioService funcionarioService;
	@Autowired private LogLoginService loginService;
	@Autowired private ConfiguracaoPaisesService paisesService;
	@Autowired private DefaultBruteForceProtectionService bf;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		boolean isLoginPost = "/sistech/login".equals(request.getRequestURI().trim()) && "POST".equalsIgnoreCase(request.getMethod().trim());
		
		if(isLoginPost) {
			
			doFilter(request, response, filterChain);
			
			var auth = SecurityContextHolder.getContext().getAuthentication();
			
			if(auth != null && auth.getPrincipal() instanceof Funcionario)  {
				 Funcionario funcionario = funcionarioService.buscaPorNome(((Funcionario) auth.getPrincipal()).getNomeFuncionario());
				 
				 boolean paisAutorizado = paisesService.paisesAtivos().contains(request.getLocale().getCountry());
				 
				if(funcionario.getAtivo() && paisAutorizado) {
					System.out.println("FUNCIONARIO LOGADO: " + funcionario.getNomeFuncionario());
					
					funcionarioService.atualizaIpLogin(funcionario, request.getRemoteHost(),request.getLocale().getCountry());
					loginService.salvaLog(new LogLogin(
							LocalDateTime.now().withNano(0),
							request.getLocalAddr(),
							request.getLocale().getCountry(),
							((request.getHeader("Host")) != null ? request.getHeader("Host") : request.getLocalAddr()),
							funcionario.getNomeFuncionario(),
							((request.getHeader("X-Real-IP")) != null ? request.getHeader("X-Real-IP") : request.getLocalAddr()),
							request.getLocalName(),
							request.getRequestURI(),
							getBrowser(request),
							"SUCESSO",
							"ATIVO"
							));
					bf.resetErroSenhaContador(request.getParameter("username"));
					
				}else {
					funcionarioService.atualizaIpLogin(funcionario, request.getRemoteHost(),request.getLocale().getCountry());
					loginService.salvaLog(new LogLogin(
							LocalDateTime.now().withNano(0),
							request.getLocalAddr(),
							request.getLocale().getCountry(),
							((request.getHeader("Host")) != null ? request.getHeader("Host") : request.getLocalAddr()),
							funcionario.getNomeFuncionario(),
							((request.getHeader("X-Real-IP")) != null ? request.getHeader("X-Real-IP") : request.getLocalAddr()),
							request.getLocalName(),
							request.getRequestURI(),
							getBrowser(request),
							"FALHA",
							funcionario.getAtivo()? "ATIVADO":"DESATIVADO"
							));
					System.out.println("USUÁRIO DESATIVADO");
					SecurityContextHolder.getContext().setAuthentication(null);
				}
					
			}else {
				
				String user = request.getParameter("username");
				int qtd = bf.registerLoginFailure(user);
				
				loginService.salvaLog(new LogLogin(
						LocalDateTime.now().withNano(0),
						request.getLocalAddr(),
						request.getLocale().getCountry(),
						((request.getHeader("Host")) != null ? request.getHeader("Host") : request.getLocalAddr()),
						user,
						((request.getHeader("X-Real-IP")) != null ? request.getHeader("X-Real-IP") : request.getLocalAddr()),
						request.getLocalName(),
						request.getRequestURI(),
						getBrowser(request),
						"ERRO SENHA",
						(qtd > 2) ? "BLOQUEADO" : "ATIVO"
						));
				
				System.out.println(user + " - USUARIO SEM SESSÃO PASSOU NO FILTRO!");
				SecurityContextHolder.clearContext();
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	String getBrowser(HttpServletRequest request) {
		
		String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();
        String os = "";
        String browser = "";

        //=================OS=======================
         if (userAgent.toLowerCase().indexOf("windows") >= 0 )
         {
             os = "Windows";
         } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
         {
             os = "Mac";
         } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
         {
             os = "Unix";
         } else if(userAgent.toLowerCase().indexOf("android") >= 0)
         {
             os = "Android";
         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
         {
             os = "IPhone";
         }else{
             os = "UnKnown, More-Info: "+userAgent;
         }
         //===============Browser===========================
        if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";
                  
        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        
        return os + " - " + browser;
	}

}
