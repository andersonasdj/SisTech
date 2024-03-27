package br.com.techgol.app.dto;

import java.time.LocalDateTime;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoSolicitacaoComFuncionario(
		Long id,
		LocalDateTime dataAbertura,
		String nomeCliente,
		String solicitante,
		String afetado,
		String descricao,
		Prioridade prioridade,
		Classificacao classificacao,
		Local local,
		Categoria categoria,
		String nomeFuncionario,
		Status status,
		boolean vip,
		boolean redFlag
		) {

		public DtoSolicitacaoComFuncionario(Solicitacao s) {
			
			this(s.getId(), 
					s.getDataAbertura(), 
					s.getCliente().getNomeCliente(), 
					s.getSolicitante(), 
					s.getAfetado(), 
					s.getDescricao(), 
					s.getPrioridade(),
					s.getClassificacao(),
					s.getLocal(),
					s.getCategoria(),
					s.getFuncionario().getNomeFuncionario(), 
					s.getStatus(),
					s.getCliente().isVip(),
					s.getCliente().isRedFlag());
		}

}
