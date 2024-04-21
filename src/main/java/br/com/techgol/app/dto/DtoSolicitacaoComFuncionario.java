package br.com.techgol.app.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;

public record DtoSolicitacaoComFuncionario(
		Long id,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime dataAbertura,
		String nomeCliente,
		String solicitante,
		String afetado,
		String descricao,
		Prioridade prioridade,
		Classificacao classificacao,
		Local local,
		Categoria categoria,
		FormaAbertura formaAbertura,
		String nomeFuncionario,
		Status status,
		String duracao,
		Integer versao,
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
					s.getFormaAbertura(),
					(s.getFuncionario()!= null) ?  s.getFuncionario().getNomeFuncionario(): " ", 
					s.getStatus(),
					(s.getDuracao() != null) ?  s.getDuracao().toString() : " ",
					s.getVersao(),
					s.getCliente().isVip(),
					s.getCliente().isRedFlag());
		}

}
