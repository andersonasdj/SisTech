package br.com.techgol.app.dto;

public record DtoDashboardCliente(
		
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
		int totalMesCorrente,
		Long totalMinutosMes) {

}
