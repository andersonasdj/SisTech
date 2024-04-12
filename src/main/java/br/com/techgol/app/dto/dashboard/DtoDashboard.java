package br.com.techgol.app.dto.dashboard;

import java.util.List;

public record DtoDashboard(
		
		int onsite,
		int offsite,
		int problema,
		int incidente,
		int solicitacao,
		int backup,
		int acesso,
		int evento,
		int baixa,
		int media,
		int alta,
		int critica,
		int planejada,
		int aberto,
		int andamento,
		int agendado,
		int aguardando,
		int pausado,
		int totalSolicitacoes,
		int totalFuncionarios,
		List<DtoDashboardResumoFuncionario> funcionario,
		int email,
		int telefone,
		int local,
		int whatsapp
		
		) {

}
