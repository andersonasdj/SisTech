package br.com.techgol.app.ia.openai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.techgol.app.ia.AIEmailAnaliseSolicitacao;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.repository.SolicitacaoRepository;

@Service
public class OpenAIService {
	
	 @Autowired private SolicitacaoRepository solicitacaoRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void resumirEmail(Long solicitacaoId, String assunto, String corpo) {
    	
    	 try {
	    	//System.out.println("* Resumindo email com OPENAI");
			//String prompt = """
			//Você é um analista de TI, Resuma este email em até 2 linhas (máx 300 caracteres), linguagem técnica:
			//	
			//Assunto: %s
			//Texto: %s
			//""".formatted(assunto, corpo);
    		//System.out.println("* Resumindo email com OPENAI");
    			
 	        String prompt = """
 	        Você é especialista em TI.
 	        Responda APENAS em JSON válido, sem explicações.

			Preencha os campos abaixo com base no email:
			
			{
			  "descricao": "...",
			  "solicitante": "...",
			  "usuarioAfetado": "...",
			  "local": "ONSITE ou OFFSITE",
			  "categoria": "...",
			  "classificacao": "...",
			  "criticidade": "..."
			}
			
			Regras:
			- descricao: resumo técnico (máx 300 caracteres)
			- solicitante: nome ou email do remetente
			- usuarioAfetado: quem será impactado (se não souber, repetir solicitante)
			- local: OFFSITE por padrão, use ONSITE apenas se mencionar local físico
			- categoria: escolher apenas da lista
			- classificacao: escolher apenas da lista
			- criticidade: definir com base na urgência
			
			Categorias possíveis:
			BACKUP, CABEAMENTO, COTACAO, DOMINIOS, EMAIL, HARDWARE, HOSPEDAGENS, INTERNET, MIGRACAO, OUTROS, PROJETO, REDE, SERVIDORES, SHAREPOINT, SMARTPHONE, SOFTWARE
			
			Classificação:
			ACESSO, BACKUP, EVENTO, INCIDENTE, PROBLEMA, SOLICITACAO
			
			Criticidade:
			ALTA, BAIXA, CRITICA, MEDIA, PLANEJADA
			
			Email:
			Assunto: %s
			Texto: %s
 	        """.formatted(assunto, corpo);
    		 
	        Map<String, Object> body = new HashMap<>();
	        body.put("model", model);
	        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
	        body.put("max_tokens", 150);
	        body.put("temperature", 0.2);
	
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBearerAuth(apiKey);
	
	        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
	
	        ResponseEntity<Map> response = restTemplate.postForEntity(
	                "https://api.openai.com/v1/chat/completions",
	                entity,
	                Map.class
	        );

	        try {

	            Map bodyResp = response.getBody();
	            List<Map<String, Object>> choices = (List<Map<String, Object>>) bodyResp.get("choices");
	            Map<String, Object> choice = choices.get(0);
	            Map<String, Object> message = (Map<String, Object>) choice.get("message");

	            String content = message.get("content").toString();

	            // LIMPA JSON (caso venha com ```json```)
	            content = content.replace("```json", "")
	                             .replace("```", "")
	                             .trim();

	            //GARANTE JSON PURO
	            if (!content.startsWith("{")) {
	                content = content.substring(content.indexOf("{"),content.lastIndexOf("}") + 1);
	            }

	            ObjectMapper mapper = new ObjectMapper();

	            AIEmailAnaliseSolicitacao analise = mapper.readValue(content, AIEmailAnaliseSolicitacao.class);

	            // SALVA NO BANCO
	            Solicitacao s = solicitacaoRepository.findById(solicitacaoId).orElseThrow();

	            s.setDescricao(analise.getDescricao());
	            s.setSolicitante(analise.getSolicitante());
	            s.setAfetado(analise.getUsuarioAfetado());

	            try {
	                s.setLocal(br.com.techgol.app.model.enums.Local.valueOf(analise.getLocal()));
	            } catch (Exception e) {}

	            try {
	                s.setCategoria(br.com.techgol.app.model.enums.Categoria.valueOf(analise.getCategoria()));
	            } catch (Exception e) {}

	            try {
	                s.setClassificacao(br.com.techgol.app.model.enums.Classificacao.valueOf(analise.getClassificacao()));
	            } catch (Exception e) {}

	            try {
	                s.setPrioridade(br.com.techgol.app.model.enums.Prioridade.valueOf(analise.getCriticidade()));
	            } catch (Exception e) {}

	            solicitacaoRepository.save(s);

	        } catch (Exception e) {
	            throw new RuntimeException("Erro ao converter JSON da IA", e);
	        }
	        
    	 } catch(Exception e) {
             System.out.println("Erro ao gerar resumo com OPENAI");
             //System.out.println(e);
         }
        
    }
    
    public boolean isDisponivel() {

        try {

        	//System.out.println("Verificando disponibilidade da IA: \n");
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            "https://api.openai.com/v1/models",
                            HttpMethod.GET,
                            entity,
                            Map.class
                    );

            return response.getStatusCode().is2xxSuccessful()
                    && response.getBody() != null;

        } catch(Exception e) {
            return false;
        }
    }
}