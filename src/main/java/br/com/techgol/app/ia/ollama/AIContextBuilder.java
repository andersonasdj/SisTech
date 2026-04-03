package br.com.techgol.app.ia.ollama;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.techgol.app.model.Solicitacao;

@Service
public class AIContextBuilder {

	public String construirContexto(String descricao, List<Solicitacao> similares) {

		StringBuilder contexto = new StringBuilder();

		contexto.append("""
				Você é um especialista em suporte técnico.

				Um novo chamado foi aberto:

				""");

		contexto.append(descricao).append("\n\n");

		contexto.append("Chamados similares resolvidos:\n");

		for (Solicitacao s : similares) {
			System.out.println("Vezes" + s.getId());

			contexto.append("""
					Problema:
					""");

			contexto.append(s.getDescricao()).append("\n");

			contexto.append("""
					Resolução:
					""");

			contexto.append(s.getResolucao()).append("\n\n");
		}

		contexto.append("""
				Sugira a provável solução para o novo chamado em uma única linha em até 150 caracteres.
				""");

		return contexto.toString();
	}
}