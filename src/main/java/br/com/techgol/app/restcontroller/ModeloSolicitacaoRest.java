package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoConjuntoModelo;
import br.com.techgol.app.dto.DtoConjuntoModeloEdit;
import br.com.techgol.app.dto.DtoModeloSolicitacao;
import br.com.techgol.app.dto.DtoSolicitacaoModelo;
import br.com.techgol.app.model.ConjuntoModelos;
import br.com.techgol.app.model.ModeloSolicitacao;
import br.com.techgol.app.repository.ConjuntoModelosRepository;
import br.com.techgol.app.repository.ModeloSolicitacaoRepository;
import br.com.techgol.app.services.ConjuntoModeloSolicitacaoService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("api/solicitacao/modelo")
public class ModeloSolicitacaoRest {

	@Autowired
	private ConjuntoModelosRepository conjuntoModelosRepository;
	
	@Autowired
	private ModeloSolicitacaoRepository modeloSolicitacaoRepository;
	
	@Autowired
	private ConjuntoModeloSolicitacaoService conjuntoModeloSolicitacaoService;
	
	@PostMapping
	public ConjuntoModelos cadastraModelo(@RequestBody DtoConjuntoModelo dado) {
		return conjuntoModeloSolicitacaoService.cadastrar(dado);
	}
	
	@GetMapping
	public List<ConjuntoModelos> listarModelo() {
		return conjuntoModeloSolicitacaoService.listarTodos();
	}
	
	@GetMapping("{id}")
	public ConjuntoModelos listarModeloPorId(@PathVariable Long id) {
		return conjuntoModeloSolicitacaoService.buscarPorId(id);
	}
	
	@PutMapping
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	public DtoConjuntoModeloEdit editarModelo(@RequestBody DtoConjuntoModeloEdit dados) {
		return conjuntoModeloSolicitacaoService.atualizarModelo(dados);
	}
	
	@PostMapping("/exemplo")
	public void cadastraModeloSolicitacao(@RequestBody DtoModeloSolicitacao dados) {
		ConjuntoModelos conjuntoModelos = conjuntoModelosRepository.getReferenceById(dados.idConjunto());
		modeloSolicitacaoRepository.save(new ModeloSolicitacao(dados, conjuntoModelos));
	}
	
	@GetMapping("/solicitacoes/{id}")
	public List<ModeloSolicitacao> listarModelosSolicitacoesPorId(@PathVariable Long id) {
		return modeloSolicitacaoRepository.buscarSolocitacoesModelosPorIdConjunto(id);
	}
	
	@GetMapping("/solicitacao/{id}")
	public ModeloSolicitacao listarModeloSolicitacaoPorId(@PathVariable Long id) {
		return modeloSolicitacaoRepository.buscarSolocitacaoModeloPorId(id);
	}
	
	@Transactional
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	@PutMapping("/solicitacao")
	public String atualizaModeloSolicitacao(@RequestBody DtoSolicitacaoModelo dados) {

		ModeloSolicitacao modeloSolicitacao = modeloSolicitacaoRepository.getReferenceById(dados.id());
		modeloSolicitacao.setCategoria(dados.categoria());
		modeloSolicitacao.setClassificacao(dados.classificacao());
		modeloSolicitacao.setDescricao(dados.descricao());
		modeloSolicitacao.setFormaAbertura(dados.formaAbertura());
		modeloSolicitacao.setLocal(dados.local());
		modeloSolicitacao.setObservacao(dados.observacao());
		modeloSolicitacao.setPrioridade(dados.prioridade());
		modeloSolicitacao.setStatus(dados.status());
		return "Atualizado com sucesso!";
	}
	
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	@DeleteMapping("/solicitacao/{id}")
	public String deletaModeloSolicitacao(@PathVariable Long id) {

		if(modeloSolicitacaoRepository.existsById(id)) {
			modeloSolicitacaoRepository.deleteById(id);
			return "Modelo deletado com sucesso!";
		}else {
			return "Não foi possível realizar a deleção!";
		}
	}
	
}
