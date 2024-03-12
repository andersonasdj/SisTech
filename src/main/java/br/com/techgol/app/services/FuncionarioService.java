package br.com.techgol.app.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.dto.DtoFuncionarioEdit;
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.dto.DtoSenha;
import br.com.techgol.app.dto.DtoSolicitacoesFuncionario;
import br.com.techgol.app.dto.DtoSolicitacoesGerais;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;

@Service
public class FuncionarioService {

	@Autowired
	FuncionarioRepository repository;

	
	public int existeFuncionarios() {
		
		return repository.existsFuncionarios();
	}
	
	
	public DtoSolicitacoesFuncionario buscaSolicitacoes(Funcionario funcionario) {
	
		int abertas, agendadas, andamento, aguardando, total;
		abertas = repository.buscaPorFuncionario(funcionario.getId(), "ABERTO");
		andamento = repository.buscaPorFuncionario(funcionario.getId(), "ANDAMENTO");
		agendadas = repository.buscaPorFuncionario(funcionario.getId(), "AGENDADO");
		aguardando = repository.buscaPorFuncionario(funcionario.getId(), "AGUARDANDO");
		total = abertas + agendadas + andamento + aguardando;
		return new DtoSolicitacoesFuncionario(abertas, andamento, agendadas, aguardando, total);
	}
	
	public DtoSolicitacoesGerais buscaSolicitacoesGerais() {
		
		int abertas, agendadas, andamento, aguardando, total;
		abertas = repository.buscaContagemGeral("ABERTO");
		andamento = repository.buscaContagemGeral("ANDAMENTO");
		agendadas = repository.buscaContagemGeral("AGENDADO");
		aguardando = repository.buscaContagemGeral("AGUARDANDO");
		total = abertas + agendadas + andamento + aguardando;
		return new DtoSolicitacoesGerais(abertas, andamento, agendadas, aguardando, total);
	}
	
	
	public Boolean existe(DtoCadastroFuncionario dados) {
		
		return repository.existsByUsername(dados.username());
	}
	
	public Boolean existePorNomeFuncionario(String nome) {
	
		return repository.existsByNomeFuncionario(nome);
	}
	
	public Funcionario buscaPorNome(String nome) {
		return repository.findBynomeFuncionario(nome);
	}
	
	public UserDetails buscaPorUserDetails(String nome) {
		return repository.findByUsername(nome);
	}
	
	
	@Transactional
	public void salvar(DtoCadastroFuncionario dados) {
		
		repository.save(new Funcionario(dados));
	}
	
	
	public List<DtoListarFuncionarios> listar() {
		
		 return repository.findAll().stream().map(DtoListarFuncionarios::new).toList();
	}
	
	
	public DtoFuncionarioEdit editar(Long id) {
		if(repository.existsById(id)) {
			return new DtoFuncionarioEdit(repository.getReferenceById(id));
		}else {
			return null;
		}
	}
	
	@Transactional
	public DtoListarFuncionarios atualizarFuncionario(DtoFuncionarioEdit dados) {
	
		Funcionario funcionario = repository.getReferenceById(dados.id());
		funcionario.setUsername(dados.username());
		funcionario.setNomeFuncionario(dados.nomeFuncionario());
		funcionario.setMfa(dados.mfa());
		funcionario.setAtivo(dados.ativo());
		funcionario.setRole(dados.role());
		funcionario.setDataAtualizacao(LocalDateTime.now());
		return new DtoListarFuncionarios(funcionario);
		
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

	@Transactional
	public String atualizarSenha(DtoSenha dados) {
		if(repository.existsById(dados.id())) {
			Funcionario f = repository.getReferenceById(dados.id());
			f.setPassword(new BCryptPasswordEncoder().encode(dados.password().toString()) );
			return "Senha alterada com sucesso!";
		}else {
			return "A senha não foi alterada";
		}

	}
	
	@Transactional
	public void atualizaDataLogin(String nome) {
		
		Funcionario funcionario = repository.buscarPorUsername(nome);
		funcionario.setDataUltimoLogin(LocalDateTime.now());
		
	}
	
}
