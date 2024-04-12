package br.com.techgol.app.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;

@Component
public class EnviaSolicitacaoCriada {
	
		@Autowired
	    private EnviadorEmail enviador;
	
		@Async("asyncExecutor")
	    public void enviar(DtoSolicitacaoComFuncionario dados){
	    	
	        enviador.enviarEmailNovaSolicitacao(dados);
	    }

}
