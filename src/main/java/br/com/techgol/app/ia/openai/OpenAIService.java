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
	    	System.out.println("* Resumindo email com OPENAI");
	
	        String prompt = """
	        Você é um analista de TI, Resuma este email em até 2 linhas (máx 300 caracteres), linguagem técnica:
	
	        Assunto: %s
	        Texto: %s
	        """.formatted(assunto, corpo);
	
	        Map<String, Object> body = new HashMap<>();
	        body.put("model", model);
	
	        body.put("messages", List.of(
	                Map.of("role", "user", "content", prompt)
	        ));
	
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
	
	        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
	        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
	
	        String resumo = message.get("content").toString().trim();
	        
	        Solicitacao s = solicitacaoRepository.findById(solicitacaoId).orElseThrow();
	
	        s.setDescricao(resumo);
	        solicitacaoRepository.save(s);
	        
    	 } catch(Exception e) {
             System.out.println("Erro ao gerar resumo com OPENAI");
             System.out.println(e);
         }
        
    }
    
    public boolean isDisponivel() {

        try {

        	System.out.println("Verificando disponibilidade da IA: \n");
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