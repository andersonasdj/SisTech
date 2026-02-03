package br.com.techgol.app.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.dto.DtoFuncionarioAdvancedEdit;
import br.com.techgol.app.dto.DtoFuncionarioAdvancedList;
import br.com.techgol.app.dto.DtoFuncionarioEdit;
import br.com.techgol.app.dto.DtoListarCustoFuncionarios;
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.dto.DtoSenha;
import br.com.techgol.app.dto.DtoSolicitacoesFuncionario;
import br.com.techgol.app.dto.DtoSolicitacoesGerais;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.orm.DtoFuncionarioEditSimplificado;
import br.com.techgol.app.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;

@Service
public class FuncionarioService {

	@Autowired
	FuncionarioRepository repository;
	
	@Transactional
	public void atualizaImagem(Long id, String caminhoFoto) {
		Funcionario funcionario = repository.getReferenceById(id);
		funcionario.setCaminhoFoto(caminhoFoto);
	}
	
	@Cacheable(value="nomesFuncionariosAtivos")
	public List<String> listarNomesFuncionariosAtivos() {
		return repository.listarNomesFuncionarios();
	}

	@Cacheable(value="idFuncionariosAtivos")
	public List<String> listarIdFuncionariosAtivos() {
		return repository.listarIdFuncionarios();
	}
	
	public List<DtoListarFuncionarios> listar() {
		return repository.listarTodosFuncionarios().stream().map(DtoListarFuncionarios::new).toList();
	}
	
	@CacheEvict(value = {"nomesFuncionariosAtivos","idFuncionariosAtivos"}, allEntries = true)
	public void salvar(DtoCadastroFuncionario dados) {
		repository.save(new Funcionario(dados));
	}
	
	@Transactional
	@CacheEvict(value = {"nomesFuncionariosAtivos","idFuncionariosAtivos"}, allEntries = true)
	public DtoListarFuncionarios atualizarFuncionario(DtoFuncionarioEdit dados) {
		
		Funcionario funcionario = repository.getReferenceById(dados.id());
		funcionario.setUsername(dados.username().toLowerCase());
		funcionario.setNomeFuncionario(dados.nomeFuncionario());
		funcionario.setMfa(dados.mfa());
		funcionario.setAusente(dados.ausente());
		funcionario.setEmail(dados.email());
		
		if(repository.count() > 1) {
			funcionario.setAtivo(dados.ativo());
		}
		funcionario.setTrocaSenha(dados.trocaSenha());
		funcionario.setDataAtualizacao(LocalDateTime.now().withNano(0));
		funcionario.setTentativasLogin(0);
		return new DtoListarFuncionarios(funcionario);
	}
	
	@Transactional
	public void alteraStatusRefeicao(Long id, boolean dados) {
		Funcionario funcionario = repository.getReferenceById(id);
		funcionario.setRefeicao( dados );
	}
	
	@Transactional
	public boolean atualizarSenha(DtoSenha dados) {
		if(repository.existsById(dados.id())) {
			if(dados.password().isBlank() || dados.password().isEmpty()) {
				return false;
			}else {
				Funcionario f = repository.getReferenceById(dados.id());
				f.setPassword(new BCryptPasswordEncoder().encode(dados.password().toString()) );
				f.setDataAtualizacao(LocalDateTime.now().withNano(0));
				f.setDataAtualizacaoSenha(LocalDateTime.now().withNano(0));
				f.setTrocaSenha(false);
				return true;
			}
		}else {
			return false;
		}
	}
	
	@Transactional
	public void atualizaIpLogin(Funcionario f, String ip, String pais) {
		Funcionario funcionario = repository.getReferenceById(f.getId());
		funcionario.setDataUltimoLogin(LocalDateTime.now().withNano(0));
		funcionario.setIp(ip);
		funcionario.setPais(pais);
	}
	
	public Boolean statusRefeicao(Long id) {
		Boolean refeicao = repository.statusRefeicao(id);
		if(refeicao != null) {
			return refeicao;
		}else return false;
	}
	
	public int existeFuncionarios() {
		return repository.existsFuncionarios();
	}
	
	public DtoFuncionarioEditSimplificado buscaDadosFuncionario(Long id) {
		return repository.buscaFuncionarioSimplificadoPorId(id);
	}
	
	public DtoSolicitacoesFuncionario buscaSolicitacoes(Funcionario funcionario) {
	
		int abertas, agendadas, andamento, aguardando, pausado, total;
		abertas = repository.buscaPorFuncionario(funcionario.getId(), "ABERTO");
		andamento = repository.buscaPorFuncionario(funcionario.getId(), "ANDAMENTO");
		agendadas = repository.buscaPorFuncionario(funcionario.getId(), "AGENDADO");
		aguardando = repository.buscaPorFuncionario(funcionario.getId(), "AGUARDANDO");
		pausado = repository.buscaPorFuncionario(funcionario.getId(), "PAUSADO");
		total = abertas + agendadas + andamento + aguardando + pausado;
		return new DtoSolicitacoesFuncionario(abertas, andamento, agendadas, aguardando, pausado, total);
	}
	
	public DtoSolicitacoesGerais buscaSolicitacoesGerais() {
		
		int abertas, agendadas, andamento, aguardando, pausado, total;
		abertas = repository.buscaContagemGeral("ABERTO");
		andamento = repository.buscaContagemGeral("ANDAMENTO");
		agendadas = repository.buscaContagemGeral("AGENDADO");
		aguardando = repository.buscaContagemGeral("AGUARDANDO");
		pausado = repository.buscaContagemGeral("PAUSADO");
		total = abertas + agendadas + andamento + aguardando + pausado;
		return new DtoSolicitacoesGerais(abertas, andamento, agendadas, aguardando, pausado, total);
	}
	
	public Boolean existe(DtoCadastroFuncionario dados) {
		return repository.existsByUsername(dados.username());
	}
	
	public Boolean existePorNomeFuncionario(String nome) {
		return repository.existsByNomeFuncionario(nome);
	}
	
	public String buscaNomeFuncionarioPorId(Long id) {
		return repository.buscarNomePorId(id);
	}
	
	public Funcionario buscaPorNome(String nome) {
		return repository.findBynomeFuncionario(nome);
	}
	
	public UserDetails buscaPorUserDetails(String nome) {
		return repository.findByUsername(nome);
	}
	
	public List<DtoListarFuncionarios> listarAtivos() {
		return repository.listarFuncionarios().stream().map(DtoListarFuncionarios::new).toList();
	}
	
	public List<DtoListarCustoFuncionarios> listarCustoAtivos() {
		return repository.listarFuncionarios().stream().map(DtoListarCustoFuncionarios::new).toList();
	}
	
	public DtoFuncionarioEdit editar(Long id) {
		if(repository.existsById(id)) {
			return new DtoFuncionarioEdit(repository.getReferenceById(id));
		}else {
			return null;
		}
	}
	
	public DtoFuncionarioEdit edicaoAvancada(Long id) {
		if(repository.existsById(id)) {
			return new DtoFuncionarioEdit(repository.getReferenceById(id));
		}else {
			return null;
		}
	}
	
	public boolean deletar(Long id) {
		if(repository.existsById(id)) {
			repository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}

	public List<String> listarNomesCliente() {
		return repository.listarNomesFuncionarios();
	}
	
	public List<Long> listarIdFuncionarosLong() {
		return repository.listarIdFuncionariosLong();
	}

	public Boolean exigeTrocaDeSenha(Long id) {
		return repository.exigeTrocaDeSenha(id);
	}

	@Transactional
	public void atualizarCode(Funcionario f) {
		Funcionario funcionario = repository.getReferenceById(f.getId());
		funcionario.setCode(f.getCode());
		
	}
	
	@Transactional
	@CacheEvict(value = {"nomesFuncionariosAtivos","idFuncionariosAtivos"}, allEntries = true)
	public DtoFuncionarioAdvancedEdit atualizarFuncionarioAdvanced(DtoFuncionarioAdvancedEdit dados) {
		
		Funcionario funcionario = repository.getReferenceById(dados.id());
		
		if(repository.count() > 1) {
			funcionario.setRole(dados.role());
		}
		funcionario.setValorHora(dados.valorHora());
		funcionario.setDataAtualizacao(LocalDateTime.now().withNano(0));
		funcionario.setTentativasLogin(0);
		return new DtoFuncionarioAdvancedEdit(funcionario);
	}
	
	public DtoFuncionarioAdvancedList advancedEdit(Long id) {
		if(repository.existsById(id)) {
			return new DtoFuncionarioAdvancedList(repository.getReferenceById(id));
		}else {
			return null;
		}
	}
}
