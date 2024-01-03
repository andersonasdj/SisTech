package br.com.techgol.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoColaboradorCadastrar;
import br.com.techgol.app.dto.DtoColaboradorListar;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.Colaborador;
import br.com.techgol.app.orm.ColaboradorProjecao;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.repository.ColaboradorRepository;

@Service
public class ColaboradorService {
	
	@Autowired
	private ColaboradorRepository repository;
	
	@Autowired
	private ClienteRepository repositoryCliente;

	public void salvar(DtoColaboradorCadastrar dados) {
		
		Cliente cliente = repositoryCliente.getReferenceById(dados.clienteId());
		repository.save( new Colaborador(dados, cliente));
	}

	public List<DtoColaboradorListar> listar() {
		return repository.findAll().stream().map(DtoColaboradorListar::new).toList();
		
	}
	
	public List<ColaboradorProjecao> listarPorIdCliente(Long id) {
		return repository.buscaColaboradoresPorIdCliente(id);
		
	}
	
	public boolean existeColaborador(Long id) {
		return repository.existsById(id);
	}
	
	public void excluirColaborador(Long id) {
		repository.deleteById(id);
	}
	
	
}
