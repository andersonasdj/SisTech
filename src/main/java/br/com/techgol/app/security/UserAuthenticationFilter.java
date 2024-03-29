package br.com.techgol.app.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private LogLoginService loginService;
	
	@Autowired
	private ConfiguracaoPaisesService paisesService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		if(request.getRequestURI().trim().equals("/sistech/login") && request.getMethod().trim().equals("POST")) {
			doFilter(request, response, filterChain);
			
			if(SecurityContextHolder.getContext().getAuthentication() != null)  {
				Funcionario funcionario = funcionarioService.buscaPorNome(((Funcionario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getNomeFuncionario());
				
				if(funcionario.getAtivo()  && paisesService.paisesAtivos().contains(request.getLocale().getCountry())) {
					System.out.println("\nDENTRO DO FILTRO!");
					System.out.println("FUNCIONARIO LOGADO: " + funcionario.getNomeFuncionario());
					System.out.println("LOCAL: " + request.getLocalName());
					System.out.println("ENDERECO IP: "+ request.getLocalAddr());
					System.out.println("PAIS: " + request.getLocale().getCountry());
					System.out.println("ENDEREÇO REMOTO: " + request.getRemoteAddr());
					System.out.println("HOST REMOTO: " + request.getRemoteHost());
					System.out.println("ROLE: " + funcionario.getRole());
					System.out.println("ATIVO : "+funcionario.getAtivo());
					System.out.println("URI: " + request.getRequestURI()+"\n");
					
					funcionarioService.atualizaIpLogin(funcionario, request.getRemoteHost(),request.getLocale().getCountry());
					loginService.salvaLog(new LogLogin(
							LocalDateTime.now().withNano(0),
							request.getLocalAddr(),
							request.getLocale().getCountry(),
							request.getRemoteHost(),
							funcionario.getNomeFuncionario(),
							request.getRemoteAddr(),
							request.getLocalName(),
							request.getRequestURI(),
							getBrowser(request),
							"SUCESSO",
							"Ativado"
							));
					
				}else {
					funcionarioService.atualizaIpLogin(funcionario, request.getRemoteHost(),request.getLocale().getCountry());
					loginService.salvaLog(new LogLogin(
							LocalDateTime.now().withNano(0),
							request.getLocalAddr(),
							request.getLocale().getCountry(),
							request.getRemoteHost(),
							funcionario.getNomeFuncionario(),
							request.getRemoteAddr(),
							request.getLocalName(),
							request.getRequestURI(),
							getBrowser(request),
							"FALHA",
							funcionario.getAtivo()? "Ativado":"Desativado"
							
							));
					System.out.println("USUÁRIO DESATIVADO");
					SecurityContextHolder.getContext().setAuthentication(null);
				}
					
			}else {
				System.out.println("USUARIO SEM SESSÃO PASSOU NO FILTRO!");
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
