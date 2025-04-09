package br.com.techgol.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("solicitacao")
public class SolicitacaoController {

	@GetMapping("/form")
	public String formulario() {
		
		return "solicitacaoForm.html";
	}

	@GetMapping("/model")
	public String formularioModelo() {
		
		return "solicitacaoFormModelo.html";
	}
	
	@GetMapping("/list")
	public String listar() {
		
		return "solicitacaoList.html";
	}
	
	@GetMapping("/modelos")
	public String modelos() {
		
		return "modelosSolicitacaoList.html";
	}
	
	@GetMapping("/legacy")
	public String legadas() {
		
		return "solicitacaoLegacyList.html";
	}
	
	@GetMapping("/relatorios")
	public String relatorios() {
		
		return "solicitacaoOpcoesRelatorios.html";
	}
	
	@GetMapping("/excluidas")
	public String excluidas() {
		
		return "solicitacaoExcluidas.html";
	}
	
	@GetMapping("/finalizadas")
	public String finalizadas() {
		
		return "solicitacaoFinalizadas.html";
	}
	
	@GetMapping("/dashboard/geral")
	public String dashboardAtivos() {
		
		return "dashboard.html";
	}
	
	@GetMapping("/dashboard/cliente")
	public String dashboardCliente() {
		
		return "dashboardCliente.html";
	}
	
	@GetMapping("/dashboard/funcionario")
	public String dashboardFuncionario() {
		
		return "dashboardFuncionario.html";
	}
	
	@GetMapping("/relatoriosclientes")
	public String relatorioCliente() {
		
		return "solicitacaoRelatorioPorCliente.html";
	}
	
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/relatoriostecnicos")
	public String relatorioTecnico() {
		
		return "solicitacaoRelatorioPorTecnico.html";
	}
	
	@GetMapping("/relatoriosfiltros")
	public String relatorioFiltros() {
		
		return "solicitacaoRelatorioPorFiltro.html";
	}
	
	@GetMapping("/relatoriosperiodo")
	public String relatorioPeriodo() {
		
		return "solicitacaoRelatorioPorPeriodo.html";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/relatoriosrendimentoperiodo")
	public String relatorioRendimentoPeriodo() {
		
		return "solicitacaoRelatorioRendimentoPorPeriodo.html";
	}

}
