package br.com.techgol.app.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.EventoDTO;
import br.com.techgol.app.dto.EventoDTOList;
import br.com.techgol.app.model.Evento;
import br.com.techgol.app.repository.EventoRepository;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
public class EventoRestController {

	@Autowired
    private EventoRepository repo;
	
	@DeleteMapping("/api/eventos/{id}")
	public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
		repo.deleteById(id);
	    return ResponseEntity.noContent().build();
	}

	@GetMapping
    public List<EventoDTO> listarEventos() {
        return repo.findAll().stream().map(EventoDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    public EventoDTOList salvar(@RequestBody Evento evento) {
    	return new EventoDTOList(repo.save(evento));
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
