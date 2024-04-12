package br.com.techgol.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoColaboradorCadastrar;
import br.com.techgol.app.dto.DtoColaboradorEdit;
import br.com.techgol.app.dto.DtoColaboradorListar;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.Colaborador;
import br.com.techgol.app.orm.ColaboradorProjecao;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.repository.ColaboradorRepository;
import jakarta.transaction.Transactional;

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
	
	@Transactional
	public String editar(DtoColaboradorEdit dados) {
		
		if(repository.existsById(dados.id())) {
			Colaborador colaborador = repository.getReferenceById(dados.id());
			colaborador.setCelular(dados.celular());
			colaborador.setNomeColaborador(dados.nomeColaborador());
			colaborador.setVip(dados.vip());
			return "Editado com sucesso!";
		}else {
			return "Colaborador não encontrado!";
		}
	}

	public List<DtoColaboradorListar> listar() {
		return repository.findAll().stream().map(DtoColaboradorListar::new).toList();
		
	}
	
	public List<ColaboradorProjecao> listarPorIdCliente(Long id) {
		return repository.buscaColaboradoresPorIdCliente(id);
	}
	
	public List<String> listarNomesIdCliente(Long id) {
		return repository.listarNomesColaboradoresPorIdCliente(id);
	}
	
	public boolean existeColaborador(Long id) {
		return repository.existsById(id);
	}
	
	public void excluirColaborador(Long id) {
		repository.deleteById(id);
	}

	public DtoColaboradorEdit editaPorIdColaborador(Long id) {
			return new DtoColaboradorEdit(repository.buscaPorId(id));	
	}
	
	
}
