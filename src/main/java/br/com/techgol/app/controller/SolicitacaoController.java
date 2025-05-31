package br.com.techgol.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("solicitacao")
public class SolicitacaoController {

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/form")
	public String formulario() {
		
		return "solicitacaoForm.html";
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/model")
	public String formularioModelo() {
		
		return "solicitacaoFormModelo.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/list")
	public String listar() {
		
		return "solicitacaoList.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/modelos")
	public String modelos() {
		
		return "modelosSolicitacaoList.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/legacy")
	public String legadas() {
		
		return "solicitacaoLegacyList.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/relatorios")
	public String relatorios() {
		
		return "solicitacaoOpcoesRelatorios.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/excluidas")
	public String excluidas() {
		
		return "solicitacaoExcluidas.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/finalizadas")
	public String finalizadas() {
		
		return "solicitacaoFinalizadas.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/dashboard/geral")
	public String dashboardAtivos() {
		
		return "dashboard.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/dashboard/cliente")
	public String dashboardCliente() {
		
		return "dashboardCliente.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/dashboard/funcionario")
	public String dashboardFuncionario() {
		
		return "dashboardFuncionario.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/relatoriosclientes")
	public String relatorioCliente() {
		
		return "solicitacaoRelatorioPorCliente.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/relatoriostecnicos")
	public String relatorioTecnico() {
		
		return "solicitacaoRelatorioPorTecnico.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/relatoriosfiltros")
	public String relatorioFiltros() {
		
		return "solicitacaoRelatorioPorFiltro.html";
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/relatoriosperiodo")
	public String relatorioPeriodo() {
		
		return "solicitacaoRelatorioPorPeriodo.html";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/relatoriosrendimentoperiodo")
	public String relatorioRendimentoPeriodo() {
		
		return "solicitacaoRelatorioRendimentoPorPeriodo.html";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/relatoriosrendimentoclienteperiodo")
	public String relatorioRendimentoClientePeriodo() {
		
		return "solicitacaoRelatorioRendimentoClientePorPeriodo.html";
	}

}
