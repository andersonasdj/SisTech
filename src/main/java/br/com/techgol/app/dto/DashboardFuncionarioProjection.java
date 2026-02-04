package br.com.techgol.app.dto;

public interface DashboardFuncionarioProjection {

    Integer getOnsite();
    Integer getOffsite();

    Integer getEmail();
    Integer getTelefone();
    Integer getLocal();
    Integer getWhatsapp();
    Integer getProativo();

    Integer getProblema();
    Integer getIncidente();
    Integer getSolicitacao();
    Integer getBackup();
    Integer getAcesso();
    Integer getEvento();

    Integer getBaixa();
    Integer getMedia();
    Integer getAlta();
    Integer getCritica();
    Integer getPlanejada();

    Integer getAberto();
    Integer getAndamento();
    Integer getAgendado();
    Integer getAguardando();
    Integer getPausado();
    Integer getFinalizado();

    Integer getTotalMesCorrente();
    Long getTotalMinutosMes();
}
