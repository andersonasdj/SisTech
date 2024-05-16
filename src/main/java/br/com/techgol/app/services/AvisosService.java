package br.com.techgol.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoAvisoCadastro;
import br.com.techgol.app.dto.DtoAvisoEdicao;
import br.com.techgol.app.dto.DtoAvisoListar;
import br.com.techgol.app.model.Avisos;
import br.com.techgol.app.repository.AvisosRepository;
import jakarta.transaction.Transactional;

@Service
public class AvisosService {
	
	@Autowired
	AvisosRepository repository;

	public long quantidadeAvisos() {
		return repository.count();
	}

	public DtoAvisoListar cadastrar(DtoAvisoCadastro dados) {
		return new DtoAvisoListar(repository.save(new Avisos(dados)));
	}

	public String deletar(Long id) {
		if(repository.existsById(id)) {
			repository.deleteById(id);
			return "Deletado com sucesso!";
		}else {
			return "Não foi possível realizar a operação";
		}
	}

	public List<DtoAvisoListar> listar() {
		if(repository.count() > 0) {
			List<DtoAvisoListar> lista = new ArrayList<>();
			
			repository.findAll().forEach(a -> {
				lista.add(new DtoAvisoListar(a.getId(), a.getAviso()));
			});
			return lista;
			
		}else {
			return null;
		}
	}

	public DtoAvisoListar buscarPorId(Long id) {
		if(repository.existsById(id)) {
			return new DtoAvisoListar(repository.getReferenceById(id));
		}else {
			throw new RuntimeException("ID buscado não existe");
		}
	}

	@Transactional
	public DtoAvisoListar atualizar(DtoAvisoEdicao dados) {
		Avisos avisos = repository.getReferenceById(dados.id());
		avisos.setAviso(dados.aviso());
		return new DtoAvisoListar(avisos.getId(), avisos.getAviso());
	}
	
}
