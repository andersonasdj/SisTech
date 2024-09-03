package br.com.techgol.app.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
import br.com.techgol.app.model.ConfiguracaoEmail;
import br.com.techgol.app.model.enums.Agendamentos;
import br.com.techgol.app.orm.SolicitacaoProjecaoCompleta;
import br.com.techgol.app.services.ConfiguracaoEmailService;

@Component
public class EnviaSolicitacaoCriada {
	
		@Autowired
	    private EnviadorEmail enviador;
		
		@Autowired
		ConfiguracaoEmailService configuracaoEmailService;
	
		@Async("asyncExecutor")
	    public void enviar(DtoSolicitacaoComFuncionario dados){
	    	
			ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.ABERTURA.toString());
			
			if(config.getEmail() != null) {
				enviador.enviarEmailNovaSolicitacao(dados, config.getEmail());
			}else {
				System.out.println("Não foi enviado email na abertura!");
			}
	    }
		
		@Async("asyncExecutor")
	    public void enviarNotificacao(SolicitacaoProjecaoCompleta dados, String destinatario){
			ConfiguracaoEmail  config = configuracaoEmailService.buscaConfiguracao(Agendamentos.ABERTURA.toString());
			
			if(config.getEmail() != null) {
				enviador.enviarEmailNotificacao(dados, destinatario);
			}else {
				System.out.println("Não foi enviado email na abertura!");
			}
	    }

}
