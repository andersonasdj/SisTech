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
import br.com.techgol.app.orm.ColaboradorProjecaoSimples;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.repository.ColaboradorRepository;
import jakarta.transaction.Transactional;

@Service
public class ColaboradorService {
	
	@Autowired
	private ColaboradorRepository repository;
	
	@Autowired
	private ClienteRepository repositoryCliente;

	public String salvar(DtoColaboradorCadastrar dados) {
		if(repository.verificaSeExistePorId(dados.clienteId(), dados.nomeColaborador()) > 0 ) {
			return "Colaborador já existe!";
		}else {
			Cliente cliente = repositoryCliente.getReferenceById(dados.clienteId());
			repository.save( new Colaborador(dados, cliente));
			return "Colaborador criado!";
		}
	}
	
	@Transactional
	public String editar(DtoColaboradorEdit dados) {
		
		if(repository.existsById(dados.id())) {
			Colaborador colaborador = repository.getReferenceById(dados.id());
			
				colaborador.setCelular(dados.celular());
				colaborador.setNomeColaborador(dados.nomeColaborador());
				colaborador.setVip(dados.vip());
				colaborador.setEmail(dados.email());
				return "Editado com sucesso!!";
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
	
	public List<ColaboradorProjecaoSimples> listarNomesCelularIdCliente(Long id) {
		return repository.listarNomesCelularColaboradoresPorIdCliente(id);
	}
	
	public String listarCelularColaborador(Long id, String nomeColaborador) {
		
		String dados = repository.listarCelularColaborador(id, nomeColaborador);
		if(dados != null) {
			String[] resultado = dados.split(",");
			return resultado[0] + (resultado[1].equals("true") ? " - VIP": "");
		}else { return ""; }
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
	
	public String retornaEmailColaboradorPorIdeEmail(Long id, String nome) {
		return repository.retornarEmailColaboradorPorIdClienteNome(id,nome);
	}
	
}
