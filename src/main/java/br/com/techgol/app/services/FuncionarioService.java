package br.com.techgol.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.dto.DtoFuncionarioEdit;
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;

@Service
public class FuncionarioService {

	@Autowired
	FuncionarioRepository repository;
	
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
	
}
