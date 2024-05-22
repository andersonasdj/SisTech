package br.com.techgol.app.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.dashboard.DtoDashboard;
import br.com.techgol.app.email.EnviadorEmail;
import br.com.techgol.app.model.ConfiguracaoEmail;
import br.com.techgol.app.model.enums.Agendamentos;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;

@Service
public class AgendamentoService {
	
	private static final String TIME_ZONE = "America/Sao_Paulo";

	@Autowired
	SolicitacaoService solicitacaoService;
	
	@Autowired
	EnviadorEmail email;
	
	@Autowired
	ConfiguracaoEmailService configuracaoEmailService;
	
	/*
	 * "0 0 * * * MON-FRI"  -> envia toda hora minuto e segundo zero nas segundas e sextas
	 * 
	 * 
	 */
	
	@Scheduled(cron = "0 */2 * * * *", zone = TIME_ZONE) //RECALCULADO A CADA 2 MINUTOS
	public void recalculoPesoSolicitacoes() {
		System.out.println("Reclassificação de peso executado!");
		solicitacaoService.recalcularPesoSolicitacoes();
	}
	
	@Scheduled(cron = "0 30 17 * * *", zone = TIME_ZONE)
	public void envioDeRelatorioDiarioSolicitacoes() {
		ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.AGENDAMENTO.toString());
		DtoDashboard dto = solicitacaoService.geraDashboard();
		email.enviarEmail(dto, "Relatório diário", config.getEmail(), "Seu relatório diário foi realizado");
	}
	
	@Scheduled(cron = "0 0 9,17 * * MON-FRI", zone = TIME_ZONE)	//AGENDADO PARA TODOS OS DIAS AS 9 e 17 HORAS - SEG a SEX
	public void envioDeAgendamentosDoDia() {
		
		ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.AGENDAMENTO.toString());
		
		LocalDateTime hoje = LocalDateTime.now().withNano(0);
		List<SolicitacaoProjecaoEntidadeComAtributos> solicitacao = solicitacaoService.buscaAgendamentosDoDia(hoje);
		
		if(config.isStatus() && !config.getEmail().isEmpty()) {
			if(solicitacao.size() >= 1) {
				email.enviarEmail(solicitacao ,"Relatório de Agendamentos para hoje", config.getEmail(), "Seu relatório diário ("+LocalDate.now()+") de agendamentos foi gerado:");
				System.out.println("Relatório de agendamentos do dia enviado com sucesso!");
			}else { System.out.println("Sem agendamentos para o dia!"); }
			
		}else {
			System.out.println("Sem configuração de email habilitado");
		}
	}
	
	@Scheduled(cron = "0 30 18 * * MON-FRI", zone = TIME_ZONE)	//AGENDADO PARA TODOS OS DIAS AS 18:30 HORAS - SEG a SEX
	public void envioDeAgendamentosDoProximoDia() {
		ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.AGENDAMENTO.toString());
		
		LocalDateTime hoje = LocalDateTime.now().withNano(0);
		List<SolicitacaoProjecaoEntidadeComAtributos> solicitacao = solicitacaoService.buscaAgendamentosDoDia(hoje.plusDays(1));
		
		if(config.isStatus() && !config.getEmail().isEmpty()) {
			if(solicitacao.size() >= 1) {
				email.enviarEmail(solicitacao ,"Relatório de Agendamentos para dia seguinte", config.getEmail(), "Seu relatório de agendamentos para o dia seguinte (" +LocalDate.now().plusDays(1)+ ") foi gerado:");
				System.out.println("Relatório de agendamentos do dia seguinte enviado com sucesso!");
			}else { System.out.println("Sem agendamentos para o dia seguinte!"); }
		}else {
			System.out.println("Sem configuração de email habilitado");
		}
	}
	
	@Scheduled(cron = "0 20 18 * * *", zone = TIME_ZONE)
	public void pausaSolicitacoesAoFinalDoDia() {
		ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.AGENDAMENTO.toString());
		
		System.out.println("Solicitações sendo pausadas");
		if(config.isStatus() && !config.getEmail().isEmpty()) {
			int qtd = solicitacaoService.pausaSolicitacoesEmAndamento();
			if(qtd >= 1) {
				email.enviarEmail("Controle de andamento das solicitações", config.getEmail(),"ATENÇÃO, " + qtd + " solicitações em andamento foram pausadas.");
			}else { System.out.println("Sem solicitações para pause!"); }
		}else {
			System.out.println("Sem configuração de email habilitado");
		}
	}
	
	@Scheduled(cron = "0 40 8,17 * * MON-FRI", zone = TIME_ZONE)	//AGENDADO PARA TODOS OS DIAS AS 18 HORAS - SEG a SEX
	public void envioDeAgendamentosAtrasados() {
		ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.AGENDAMENTO.toString());
		
		List<SolicitacaoProjecaoEntidadeComAtributos> solicitacoes = solicitacaoService.buscaAgendamentosAtrasados();
		
		if(config.isStatus() && !config.getEmail().isEmpty()) {
			if(solicitacoes.size() >= 1) {
				email.enviarEmail(solicitacoes ,"Relatório de Agendamentos atrasados", config.getEmail(), "ATENÇÃO! Existem agendamentos atrasados!");
				System.out.println("Relatório de agendamentos atrasados enviado com sucesso!");
			}else { System.out.println("Sem agendamentos agendamentos atrasados!"); }
		}else {
			System.out.println("Sem configuração de email habilitado");
		}
	}
	
	
	
	
}
