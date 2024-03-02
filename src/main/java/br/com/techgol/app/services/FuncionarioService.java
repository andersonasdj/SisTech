package br.com.techgol.app.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.dto.DtoFuncionarioEdit;
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.dto.DtoSenha;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;

@Service
public class FuncionarioService {

	@Autowired
	FuncionarioRepository repository;
	
	
	public Boolean existe(DtoCadastroFuncionario dados) {
		
		return repository.existsByUsername(dados.username());
	}
	
	public Boolean existePorNomeFuncionario(String nome) {
	
		return repository.existsByNomeFuncionario(nome);
	}
	
	public Funcionario buscaPorNome(String nome) {
		return repository.findBynomeFuncionario(nome);
	}
	
	
	@Transactional
	public void salvar(DtoCadastroFuncionario dados) {
		
		Funcionario funcionario = repository.save(new Funcionario(dados));
		System.out.println(funcionario);
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
		funcionario.setDataAtualizacao(new Date());
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
	
}
