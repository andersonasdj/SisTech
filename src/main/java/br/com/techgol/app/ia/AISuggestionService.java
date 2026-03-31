package br.com.techgol.app.ia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.repository.SolicitacaoRepository;

@Service
public class AISuggestionService {

    @Autowired private SolicitacaoRepository solicitacaoRepository;
    @Autowired private AIContextBuilder contextBuilder;
    @Autowired private OllamaClient ollamaClient;

    
//    public String sugerirSolucao(String descricao) {
//
//        List<Solicitacao> similares = solicitacaoRepository.buscarChamadosSemelhantes(descricao);
//        String contexto = contextBuilder.construirContexto(descricao, similares);
//        return ollamaClient.gerarResposta(contexto);
//    }
    
    @Async
    public void gerarSugestaoAsync(Solicitacao solicitacao) {

        String sugestao = sugerirSolucao(solicitacao.getDescricao());
        solicitacao.setSugestaoIA(sugestao);
        System.out.println(sugestao);
        solicitacaoRepository.save(solicitacao);
    }
    
    public String sugerirSolucao(String descricao) {
    	
        List<Solicitacao> similares = solicitacaoRepository.buscarChamadosSemelhantes(descricao);
        String contexto = contextBuilder.construirContexto(descricao, similares);
        return ollamaClient.gerarResposta(contexto);
    }
    
   
    public String resumirEmail(String assunto, String corpo) {

        String prompt = """
    Você é um analista de suporte técnico.

    Resuma o email abaixo em uma descrição objetiva para um chamado.

    Regras:
    - máximo 350 caracteres
    - linguagem técnica
    - não incluir assinatura ou histórico

    Assunto:
    """ + assunto + """

    Corpo do email:
    """ + corpo + """

    Resumo:
    """;

        return ollamaClient.gerarResposta(prompt);
    }
    
    public String analisarEmail(String assunto, String corpo) {

        String prompt = """
    Você é um analista de suporte técnico.

    Analise o email abaixo e gere um JSON estruturado.
	Campos obrigatórios:
	
	descricao: resumo técnico do problema (máximo 300 caracteres)
	
	classificacao:
	(PROBLEMA, INCIDENTE, SOLICITACAO, BACKUP, ACESSO, EVENTO)
	
	categoria:
	(HARDWARE, SOFTWARE, REDE, CABEAMENTO, SMARTPHONE, PROJETO, BACKUP, OUTROS, EMAIL, SHAREPOINT, INTERNET, SERVIDORES, COTACAO, MIGRACAO, DOMINIOS, HOSPEDAGENS)
	
	prioridade:
	(BAIXA, MEDIA, ALTA, CRITICA, PLANEJADA)
	
	afetado:
	nome da pessoa ou setor afetado pelo problema.
	
	Responda apenas com JSON.

    Email assunto:
    """ + assunto + """

    Email corpo:
    """ + corpo + """
    """;

        return ollamaClient.gerarResposta(prompt);
    }

    public boolean isDisponivel() {

        try {

            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(1000);
            factory.setReadTimeout(1000);

            RestTemplate restTemplate = new RestTemplate(factory);

            ResponseEntity<String> response =
                    restTemplate.getForEntity(
                            "http://localhost:11434/api/tags",
                            String.class
                    );

            return response.getStatusCode().is2xxSuccessful();

        } catch(Exception e) {

            return false;

        }
    }
    
    @Async
    public void processarResumoAsync(Long solicitacaoId, String assunto, String corpo) {

        try {
        	System.out.println("Resumindo email com IA");

            String resumo = resumirEmail(assunto, corpo);

            Solicitacao s = solicitacaoRepository
                    .findById(solicitacaoId)
                    .orElseThrow();

            s.setDescricao(resumo);

            solicitacaoRepository.save(s);

        } catch(Exception e) {

            System.out.println("Erro ao gerar resumo IA");

        }

    }
    
    
}