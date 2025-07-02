package br.com.techgol.app.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoComputadorResumido;
import br.com.techgol.app.email.EnviadorEmail;
import br.com.techgol.app.model.Computador;
import br.com.techgol.app.model.ConfiguracaoEmail;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Agendamentos;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.repository.ComputadorRepository;
import br.com.techgol.app.repository.SolicitacaoRepository;
import jakarta.transaction.Transactional;

@Service
public class ComputadorService {

    private final SolicitacaoRepository solicitacaoRepository;
	
	@Autowired ComputadorRepository repository;
	@Autowired FuncionarioService funcionarioService;
	@Autowired EnviadorEmail email;
	@Autowired ConfiguracaoEmailService configuracaoEmailService;


    ComputadorService(SolicitacaoRepository solicitacaoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
    }
	
	
	@Transactional
	public String atualizarComputador(DtoComputadorResumido dto) {
		
		if(repository.existsById(dto.id())) {
			Computador computador = repository.getReferenceById(dto.id());
			computador.setComment(dto.comment());
			computador.setStorageMonitor(dto.storageMonitor());
			computador.setStatusMonitor(dto.statusMonitor());
			return "Computador atualizado com sucesso!";
		}else {
			return "Não foi possível atualizar!";
		}
		
	}

	public List<Computador> listarComputadoresOffline() {
		LocalDateTime limite = LocalDateTime.now().minusMinutes(5);
		return repository.listarComputadoresOffline(limite);
	}

	public void abrirSolicitacoesHostsOff(List<Computador> computadores) {
		
		if(!computadores.isEmpty()) {
			Funcionario funcionario = funcionarioService.buscaPorNome("Suporte");
			ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.AGENDAMENTO.toString());
			if(funcionario != null) {
				computadores.forEach( c -> {
					if(solicitacaoRepository.findByDescricaoAndStatus("HOST OFFLINE - "+c.getName(), Status.ABERTO) == null) {
						Solicitacao s = new Solicitacao(c, funcionario);
						solicitacaoRepository.save(s);
						email.enviarEmailHostOffline(s, "HOST OFFLINE - " + c.getName(), config.getEmail(), "ALERTA - ");
					}
				});
			}
		}
		
	}

}
