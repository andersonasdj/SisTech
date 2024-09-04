package br.com.techgol.app.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.TimeRefeicao;
import br.com.techgol.app.orm.RefeicaoProjecao;
import br.com.techgol.app.repository.FuncionarioRepository;
import br.com.techgol.app.repository.TimeRefeicaoRepository;
import jakarta.transaction.Transactional;

@Service
public class TimeRefeicaoServices {

	@Autowired
	TimeRefeicaoRepository refeicaoRepository;
	
	@Autowired
	FuncionarioRepository funcionarioRepository;

	@Transactional
	public boolean alteraStatus(Long id, LocalDateTime inicio, LocalDateTime fim) {
		
		if(refeicaoRepository.existeUpdateHoje(id, inicio, fim) != 0) {
			System.out.println("JA EXISTE REFEICAO FINALIZADA");
			return false;
		}else if(refeicaoRepository.existeFlagRefeicaoHoje(id, inicio, fim) != null) {
			System.out.println("EXISTE REFEICAO EM ANDAMENTO");
			TimeRefeicao refeicao = refeicaoRepository.existeFlagRefeicaoHoje(id, inicio, fim);
			refeicao.setFim(LocalDateTime.now().withNano(0));
			refeicao.setDuracao(Duration.between(refeicao.getInicio(), LocalDateTime.now()).toMinutes());
			return true;
		}else {
			System.out.println("CADASTRADO HORARIO DE REFEICAO");
			Funcionario funcionario = funcionarioRepository.getReferenceById(id);
			refeicaoRepository.save(new TimeRefeicao(LocalDateTime.now().withNano(0), funcionario));
			return true;
		}
	}
	
	public List<RefeicaoProjecao> listarUltimos50() {
		return refeicaoRepository.listarUltimos50();
	}
	
}
