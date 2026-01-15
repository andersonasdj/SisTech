package br.com.techgol.app.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.services.FuncionarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AppController {
	
	@Autowired
	FuncionarioService funcionarioService;
	
    @GetMapping("/2fa")
    public String exibirFormulario2FA(HttpSession session, Model model) {
        Funcionario tempUser = (Funcionario) session.getAttribute("TEMP_USER");

        if (tempUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("email", tempUser.getEmail());
        return "templates/2fa.html";
    }
    
    @PostMapping("/verify-2fa")
    public String verificarCodigo2FA(@RequestParam String codigo, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        Funcionario tempUser = (Funcionario) session.getAttribute("TEMP_USER");
        if (tempUser == null) {
            return "redirect:/login";
        }

        Funcionario funcionarioBanco = funcionarioService.buscaPorNome(tempUser.getNomeFuncionario());

        if (funcionarioBanco.getCode() != null && funcionarioBanco.getCode().equals(codigo)) {

            // Marca como verificado para mudar autoridade para ROLE_USER
            funcionarioBanco.setTwoFactorVerified(true);
            funcionarioBanco.setCode(null);
            funcionarioService.atualizarCode(funcionarioBanco);

            // Recupera authorities reais
            Collection<? extends GrantedAuthority> authorities = funcionarioBanco.getAuthorities();

            // Promove a autenticação
            UsernamePasswordAuthenticationToken fullAuth = new UsernamePasswordAuthenticationToken(funcionarioBanco, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(fullAuth);
            
            session.removeAttribute("TEMP_USER");
            return "redirect:/home";
        } else {
            request.setAttribute("erro", "Código inválido");
            return "redirect:/2fa?error="+ URLEncoder.encode("Código inválido!", StandardCharsets.UTF_8);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/impressao-timesheet-cliente")
	public String impressaoTimeSheetCliente() {
		return "templates/impressao-timesheet-cliente.html";
	}
    
    @PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/impressao-timesheet-funcionario")
	public String impressaoTimeSheetFuncionario() {
		return "templates/impressao-timesheet-funcionario.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/upload")
	public String teste() {
		return "templates/upload.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/impressao")
	public String impressaoSolicitacao() {
		return "templates/impressao.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/impressao-tag")
	public String impressaoSolicitacaoTag() {
		return "templates/impressao-tag.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/impressao-relatorio")
	public String impressaoRelatorioCliente() {
		return "templates/impressao-relatorio-cliente.html";
	}
	
	@GetMapping("/login")
	public String login() {
		return "templates/login.html";
	}
	
	@GetMapping("/mfa")
	public String mfa() {
		return "templates/mfa.html";
	}
	
	@GetMapping("/create")	//ENDPOINT PARA CRIACAO DO PRIMEIRO USUARIO DO SISTEMA
	public String create() {
		if(funcionarioService.existeFuncionarios() == 0) {
			return "templates/create.html";
		}else {
			return "/logout";
		}
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/home")
	public String home() {
		return "templates/home.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/sobre")
	public String sobre() {
		return "templates/sobre.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/avisos")
	public String avisos() {
		return "templates/avisos.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/funcionalidades")
	public String funcionalidades() {
		return "templates/funcionalidade.html";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/logAcesso")
	public String logsAcesso() {
		return "templates/logsAcesso.html";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/logRefeicao")
	public String logsRefeicao() {
		return "templates/logsRefeicao.html";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/gerencia")
	public String gerencia() {
		return "templates/gerencia.html";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/configuracoes")
	public String configuracoes() {
		return "templates/configuracoes.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/calendario")
	public String calendario() {
		return "templates/calendario.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/calendario2")
	public String calendario2() {
		return "templates/calendario2.html";
	}
}
