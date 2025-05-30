package br.com.techgol.app.dto;

import java.util.List;

public record DtoDashboardFuncionarios(
		
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
		int finalizado,
		int totalSolicitacoes,
		Long totalMinutosMes,
		int totalMesCorrente,
		int email,
		int telefone,
		int local,
		int whatsapp,
		int proativo,
		List<DtoHistorico> historico,
		List<DtoHistoricoDias> historicoDias) {

}
