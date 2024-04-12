package br.com.techgol.app.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.techgol.app.dto.dashboard.DtoDashboard;

@Component
public class EnviaRelatorioDiario {
	
		@Autowired
	    private EnviadorEmail enviador;
	
	    public void enviar(DtoDashboard dados, String assunto, String destinatario, String texto ){
	    	
	        enviador.enviarEmail(dados, assunto, destinatario, texto);
	    }

}
