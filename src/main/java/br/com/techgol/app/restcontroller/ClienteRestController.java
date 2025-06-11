package br.com.techgol.app.restcontroller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.techgol.app.dto.DadosClienteEditDTO;
import br.com.techgol.app.dto.DtoAtualizarCliente;
import br.com.techgol.app.dto.DtoCadastroCliente;
import br.com.techgol.app.dto.DtoClienteList;
import br.com.techgol.app.dto.DtoToken;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.services.ClienteService;

@RestController
@RequestMapping("clientes")
public class ClienteRestController {
	
	@Value("${sistech.security.agentkey}")
    private String AGENT_KEY;
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private ClienteService service;
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/gettoken")
	public ResponseEntity<String> enviar() {
	    System.out.println("TESTE");

	    DtoToken dados = new DtoToken(AGENT_KEY);

	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new JavaTimeModule());
	        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	        String json = mapper.writeValueAsString(dados);

	        HttpClient client = HttpClient.newHttpClient();
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create("http://localhost:8080/agent/api/auth/token"))
	                .header("Content-Type", "application/json")
	                .POST(HttpRequest.BodyPublishers.ofString(json))
	                .build();

	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	        System.out.println("Status: " + response.statusCode());
	        System.out.println("Resposta: " + response.body());

	        JsonNode root = mapper.readTree(response.body());
	        String token = root.path("token").asText(); 

	        return ResponseEntity.ok(token);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Erro ao obter token");
	    }
	}

	
	@GetMapping("nome/{conteudo}") //RETORNA DTO COM PROJEÇÃO DOS DADOS NECESSÀRIO COM NATIVE QUERY
	public Page<DtoClienteList> buscarClientePorPalavras(@PathVariable String conteudo,  @PageableDefault(size = 100, sort= {"id"}, direction = Direction.DESC) Pageable page) {
		return service.listarClientePorPalavra(page,conteudo);
	}
	
	@GetMapping
	public ResponseEntity<Page<DtoClienteList>> listar(@PageableDefault(size = 15, sort= {"nomeCliente"}, direction = Direction.ASC) Pageable page){
		var todosOsClientes = service.listarTodos(page);
		return ResponseEntity.ok(todosOsClientes);
	}
	
	@GetMapping("/filtro/{bairro}")
	public List<DtoClienteList> listarFiltro(@PathVariable String bairro){
		return repository.listarClientesPorBairro(bairro).stream().map(DtoClienteList::new).toList();
	}
	
	@GetMapping("/filtro/vip/{vip}")
	public List<DtoClienteList> listarFiltroVip(@PathVariable String vip){
		if(vip.equals("vip")) {
			return repository.listarClientesVip().stream().map(DtoClienteList::new).toList();
		}else if(vip.equals("redflag")) {
			return repository.listarClientesRedFlag().stream().map(DtoClienteList::new).toList();
		}else {
			return repository.listarClientesRedFlagEVip().stream().map(DtoClienteList::new).toList();
		}
	} 
	
	@GetMapping("/nomes")
	public List<String> listaClientesNome(){
		return repository.listarNomesCliente();
	}
	
	@GetMapping("/edit/{id}")
	public DadosClienteEditDTO editar(@PathVariable Long id ) {
		if(repository.existsById(id)) {
			return new DadosClienteEditDTO(repository.getReferenceById(id));			
		}else {
			return null;
		}
	}
	
	@PostMapping
	public void cadastrar(@RequestBody DtoCadastroCliente dados) {
		service.cadastrarNovoCliente(dados);
	}
	
	@PutMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public DtoAtualizarCliente atualizar(@RequestBody DtoAtualizarCliente dados) {
		return new DtoAtualizarCliente(service.atualizarCliente(dados));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/{id}")
	public void deletar(@PathVariable Long id ) {
		repository.deleteById(id);
	}
	
	@GetMapping("/bairros")
	public List<String> listaBairrosClientes(){
		return service.listarBairrosClientes();
	}
	
}
