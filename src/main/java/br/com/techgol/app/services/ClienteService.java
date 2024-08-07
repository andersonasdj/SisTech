package br.com.techgol.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	ClienteRepository repository;

	public Cliente buscaClientePorNome(Long dados) {
		return  repository.getReferenceById(dados);
	}


	public List<String> listarNomesClienteAtivos() {
		return repository.listarNomesClienteAtivos();
	}

	public List<String> listarIdClienteAtivos() {
		return repository.listarIdClienteAtivos();
	}
	
	public boolean verificaSeVip(Long id) {
		return repository.verificaSeVip(id);
	}
	
	public boolean verificaSeRedFlag(Long id) {
		return repository.verificaSeRedFlag(id);
	}
	
	public List<String> listarBairrosClientes(){
		List<String> listaDeBairros = new ArrayList<>();
		List<String> listaCompletaBairros = repository.listarBairrosClientes();
		
		listaCompletaBairros.forEach( b -> {
			if(!listaDeBairros.contains(b)) {
				listaDeBairros.add(b);
			}
		});
		return listaDeBairros;
	}

}
