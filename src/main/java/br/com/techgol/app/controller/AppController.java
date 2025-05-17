package br.com.techgol.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
	
	// Exibe o formulário de verificação de 2FA
    @GetMapping("/2fa")
    public String exibirFormulario2FA(HttpSession session, Model model) {
        Funcionario tempUser = (Funcionario) session.getAttribute("TEMP_USER");

        if (tempUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("email", tempUser.getEmail()); // Se quiser exibir para qual email foi enviado
        return "templates/2fa.html"; // Nome do template (ex: templates/2fa.html ou 2fa.jsp)
    }
    
 // Valida o código 2FA
    @PostMapping("/verify-2fa")
    public String verificarCodigo2FA(@RequestParam("codigo") String codigo, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        Funcionario tempUser = (Funcionario) session.getAttribute("TEMP_USER");
        if (tempUser == null) {
            return "redirect:/login";
        }

        // Recarrega o usuário do banco para pegar o código mais recente
        Funcionario funcionarioBanco = funcionarioService.buscaPorNome(tempUser.getNomeFuncionario());

        if (funcionarioBanco.getCode() != null && funcionarioBanco.getCode().equals(codigo)) {
            // Código correto → autenticar manualmente
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    funcionarioBanco, null, funcionarioBanco.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Invalida código após uso (opcional)
            funcionarioBanco.setCode(null);
            funcionarioService.atualizarCode(funcionarioBanco); // ou o método adequado

            // Remove usuário temporário da sessão
            session.removeAttribute("TEMP_USER");

            return "redirect:/home";
        } else {
            request.setAttribute("erro", "Código inválido");
            return "2fa";
        }
    }
	
	
	@GetMapping("/impressao-timesheet-cliente")
	public String impressaoTimeSheetCliente() {
		return "templates/impressao-timesheet-cliente.html";
	}
	
	@GetMapping("/upload")
	public String teste() {
		return "templates/upload.html";
	}
	
	@GetMapping("/impressao")
	public String impressaoSolicitacao() {
		return "templates/impressao.html";
	}
	
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
	
	@GetMapping("/home")
	public String home() {
		return "templates/home.html";
	}
	
	@GetMapping("/sobre")
	public String sobre() {
		return "templates/sobre.html";
	}
	
	@GetMapping("/avisos")
	public String avisos() {
		return "templates/avisos.html";
	}
	
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

}
