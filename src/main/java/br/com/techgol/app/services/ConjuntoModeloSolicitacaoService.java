package br.com.techgol.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoConjuntoModelo;
import br.com.techgol.app.dto.DtoConjuntoModeloEdit;
import br.com.techgol.app.model.ConjuntoModelos;
import br.com.techgol.app.repository.ConjuntoModelosRepository;
import jakarta.transaction.Transactional;

@Service
public class ConjuntoModeloSolicitacaoService {

	@Autowired
	ConjuntoModelosRepository repository;

	@Cacheable(value="nomesConjuntoModelos")
	public List<String> listarNomesModelos() {
		return repository.listarNomesModelos();
	}

	@Cacheable(value="idConjuntoModelos")
	public List<String> listarIdModelos() {
		return repository.listarIdModelos();
	}

	@Cacheable(value="listarTodosConjuntoModelos")
	public List<ConjuntoModelos> listarTodos() {
		return repository.findAll();
	}
	
	@Transactional
	@CacheEvict(value = {"nomesConjuntoModelos","idConjuntoModelos","listarTodosConjuntoModelos"}, allEntries = true)
	public DtoConjuntoModeloEdit atualizarModelo(DtoConjuntoModeloEdit dados) {
		ConjuntoModelos conjuntoModelo = repository.getReferenceById(dados.id());
		conjuntoModelo.setNomeModelo(dados.nomeModelo());
		return new DtoConjuntoModeloEdit(conjuntoModelo.getId(), conjuntoModelo.getNomeModelo());
	}
	
	public ConjuntoModelos cadastrar(DtoConjuntoModelo dado) {
		return repository.save(new ConjuntoModelos(dado));
	}

	public ConjuntoModelos buscarPorId(Long id) {
		return repository.buscaPorId(id);
	}
}
