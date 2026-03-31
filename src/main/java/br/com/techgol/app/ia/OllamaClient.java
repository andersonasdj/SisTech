package br.com.techgol.app.ia;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OllamaClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String gerarResposta(String prompt) {
    	System.out.println("Em client ollama");

//        String url = "http://localhost:11434/api/generate";
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("model", "llama3");
//        body.put("prompt", prompt);
//        body.put("stream", true);
//        body.put("keep_alive", "5m");
//        
//        
//        Map<String, Object> options = new HashMap<>();
//        options.put("num_predict", 350); // limite da resposta
//        options.put("temperature", 0.2);
//        options.put("top_p", 0.9);
//        
//        body.put("options", options);
    	String url = "http://localhost:11434/api/generate";

    	Map<String, Object> body = new HashMap<>();
    	body.put("model", "phi3"); // MAIS LEVE
    	body.put("prompt", prompt);
    	body.put("stream", false);
    	body.put("keep_alive", "5m");

    	Map<String, Object> options = new HashMap<>();
    	options.put("num_predict", 80);   // CRÍTICO
    	options.put("temperature", 0.2);
    	options.put("top_p", 0.9);

    	body.put("options", options);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        return response.getBody().get("response").toString();
    }
}