package br.com.techgol.app.dto;

import java.util.List;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoDadosEdicaoRapidaMaisFuncionarios(
		
		Long id,
		String descricao,
		String resolucao,
		String observacao,
		Status status,
		Categoria categoria,
		Classificacao classificacao,
		Prioridade prioridade,
		Local local,
		String funcionario,
		String dataAgendado,
		String horaAgendado,
		String dataAtualizacao,
		String solicitante,
		String afetado,
		FormaAbertura formaAbertura,
		Integer versao,
		List<String> funcionarios,
		List<String> colaboradores) {

	public DtoDadosEdicaoRapidaMaisFuncionarios(Solicitacao dados, List<String> s, List<String> c) {
		this(
				dados.getId(),
				dados.getDescricao(),
				dados.getResolucao(),
				dados.getObservacao(),
				dados.getStatus(),
				dados.getCategoria(),
				dados.getClassificacao(),
				dados.getPrioridade(),
				dados.getLocal(),
				dados.getFuncionario().getNomeFuncionario(),
				(dados.getDataAgendado()!= null) ? dados.getDataAgendado().toLocalDate().toString() : "",
				(dados.getDataAgendado()!= null) ? dados.getDataAgendado().toLocalTime().toString() : "",
				(dados.getDataAtualizacao().toLocalDate() + " - "+ dados.getDataAtualizacao().toLocalTime()).toString(),
				dados.getSolicitante(),
				dados.getAfetado(),
				dados.getFormaAbertura(),
				dados.getVersao(),
				s,
				c);
	}

}
