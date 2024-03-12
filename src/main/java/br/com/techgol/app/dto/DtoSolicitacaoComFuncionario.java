package br.com.techgol.app.dto;

import java.util.Date;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoSolicitacaoComFuncionario(
		Long id,
		Date dataAbertura,
		String nomeCliente,
		String solicitante,
		String afetado,
		String descricao,
		Prioridade prioridade,
		String nomeFuncionario,
		Status status
		) {

		public DtoSolicitacaoComFuncionario(Solicitacao s) {
			
			this(s.getId(), 
					s.getDataAbertura(), 
					s.getCliente().getNomeCliente(), 
					s.getSolicitante(), 
					s.getAfetado(), 
					s.getDescricao(), 
					s.getPrioridade(), 
					s.getFuncionario().getNomeFuncionario(), 
					s.getStatus());
		}

}
