package br.com.techgol.app.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
import br.com.techgol.app.dto.dashboard.DtoDashboard;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;
import jakarta.mail.internet.MimeMessage;

@Component
public class EnviadorEmail {
	
		@Autowired
	    private JavaMailSender emailSender;

		@Async
	    public void enviarEmailNovaSolicitacao(DtoSolicitacaoComFuncionario dados, String destinatario) {
	        try {
	            
	            MimeMessage message = emailSender.createMimeMessage();
	            message.setSubject("Cadastro de nova solicitação");
	            MimeMessageHelper helper;
	            helper = new MimeMessageHelper(message, true);
	            
	            helper.setFrom("noreply.providerone@gmail.com");
	            helper.setTo(destinatario);
	            helper.setText(""
	            		+ "<h5> Abertura de solicitação: </h5>"
	            		+ "<br />"
	            		+ "<p>Data Abertura: <b>" + dados.dataAbertura() +"</b></p>"
	            		+ "<p>Nome do Cliente: <b>" + dados.nomeCliente() +"</b></p>"
	            		+ "<p>Nome do Solicitanre: <b>" + dados.solicitante() +"</b></p>"
	            		+ "<p>Nome do Afetado: <b>" + dados.afetado() +"</b></p>"
	            		+ "<p>Descrição: <b>" + dados.descricao() +"</b></p>"
	            		+ "<p>Prioridade: <b>" + dados.prioridade() +"</b></p>"
	            		+ "<p>Local: <b>" + dados.local() +"</b></p>"
	            		+ "<p>Categoria: <b>" + dados.categoria() +"</b></p>"
	            		+ "<p>Nome do técnico: <b>" + dados.nomeFuncionario() +"</b></p>"
	            		+ "<p>Status: <b>" + dados.status() +"</b></p>",
	            		true);
	            emailSender.send(message);
	            
	            System.out.println("Enviando email!");
	            System.out.println("OK");

	            //Simulando demora de 3 segundos para enviar email
	            //Thread.sleep(3000);

	        } catch (Exception e) {
	            throw new RuntimeException("Erro ao enviar email!", e);
	        }
	    }
		
		@Async
	    public void enviarEmail(DtoSolicitacaoComFuncionario dados, String destinatario) {
	        try {
	            var email = new SimpleMailMessage();
	            email.setFrom("noreply.providerone@gmail.com");
	            email.setSubject("Teste");
	            email.setTo(destinatario);
	            email.setText("Data Abertura: " + dados.dataAbertura() +"\n"
	            		+ "Nome do Cliente: " + dados.nomeCliente() +"\n"
	            		+ "Nome do Solicitanre: " + dados.solicitante() +"\n"
	            		+ "Nome do Afetado: " + dados.afetado() +"\n"
	            		+ "Descrição: " + dados.descricao() +"\n"
	            		+ "Prioridade: " + dados.prioridade() +"\n"
	            		+ "Local: " + dados.local() +"\n"
	            		+ "Categoria: " + dados.categoria() +"\n"
	            		+ "Nome do técnico: " + dados.nomeFuncionario() +"\n"
	            		+ "Status: " + dados.status() +"\n");
	        	emailSender.send(email);
	            System.out.println("Enviando email!");
	            System.out.println("OK");

	            //Simulando demora de 3 segundos para enviar email
	            //Thread.sleep(3000);

	        } catch (Exception e) {

	            throw new RuntimeException("Erro ao enviar email!", e);
	        }
	    }

		public void enviarEmail(DtoDashboard dto, String assunto, String destinatario, String texto) {
			var email = new SimpleMailMessage();
            email.setFrom("noreply.providerone@gmail.com");
            email.setSubject(assunto);
            email.setTo(destinatario);
            email.setText(texto
            		+ " Total de Solicitações:  " + dto.totalSolicitacoes() + "\n"
            		+ "  * ABERTAS: " + dto.aberto() + "\n"
            		+ "  * AGENDADAS: " + dto.agendado() + "\n"
            		+ "  * AGUARDANDO: " + dto.aguardando() + "\n"
            		+ "  * ANDAMENTO: " + dto.andamento() + "\n"
            		+ "  * PAUSADAS: " + dto.pausado() + "\n"
            		+ "  \n############################################### \n"
            		+ " Local das solicitações:  \n"
            		+ "  * ONSITE: " + dto.onsite() + "\n"
            		+ "  * OFFSITE: " + dto.offsite() + "\n"
            		+ "  \n############################################### \n"
            		+ " Classificação das Solicitações:  \n"
            		+ "  * INCIDENTE: " + dto.incidente() + "\n"
            		+ "  * PROBLEMA: " + dto.problema() + "\n"
            		+ "  * SOLICITAÇÃO: " + dto.solicitacao() + "\n"
            		+ "  * ACESSO: " + dto.acesso() + "\n"
            		+ "  * EVENTO: " + dto.evento() + "\n"
            		+ "  * BACKUP: " + dto.backup() + "\n"
            		+ "  \n############################################### \n"
            		+ " Prioridade das Solicitações:  \n"
            		+ "  * BAIXA: " + dto.baixa() + "\n"
            		+ "  * MEDIA: " + dto.media() + "\n"
            		+ "  * ALTA: " + dto.alta() + "\n"
            		+ "  * CRITICA: " + dto.critica() + "\n"
            		+ "  * PLANEJADA: " + dto.planejada() + "\n"
            		
            		);
        	emailSender.send(email);
            System.out.println("Enviando email Relatório diário!");
            System.out.println("OK");
			
		}

		public void enviarEmail(List<SolicitacaoProjecaoEntidadeComAtributos> solicitacao, String assunto, String destinatario, String texto) {
			
			String corpoEmail = "<html><body>"
					+ "<style>"
					+ "table, th, td {\n"
					+ "  border: 1px solid white;\n"
					+ "  border-collapse: collapse;\n"
					+ "  border-spacing: 15px 10px;\n"
					+ "}\n"
					+ "table>tbody>tr>td {\n"
					+ " padding: 5px 20px;\n"
					+ "	background-color: #FFED8A;\n"
					+ "}"
					+ "</style>"
					+ "<br /><h3> " + texto +" </h3><br /> "
					+ "<table style=\"text-align: center;\" cellspacing=\"3\" border=\"1\">\n"
					+ "				<thead>\n"
					+ "					<tr>\n"
					+ "						<th scope=\"col\">ID</th>\n"
					+ "						<th scope=\"col\">Data Abertura</th>\n"
					+ "						<th scope=\"col\">Cliente</th>\n"
					+ "						<th scope=\"col\">Solicitante</th>\n"
					+ "						<th scope=\"col\">Afetado</th>\n"
					+ "						<th scope=\"col\">Classificações</th>\n"
					+ "						<th scope=\"col\">Descrição</th>\n"
					+ "						<th scope=\"col\">Prioridade</th>\n"
					+ "						<th scope=\"col\">Responsável</th>\n"
					+ "						<th scope=\"col\">Status</th>\n"
					+ "					</tr>\n"
					+ "				</thead>\n"
					+ "				<tbody>";
			
			for(int i=0; i < solicitacao.size(); i++ ) {
				corpoEmail += "<tr>"
								+ "<td>" + solicitacao.get(i).getId() + "</td>"
								+ "<td>" + solicitacao.get(i).getDataAbertura() + "</td>"
								+ "<td>" + solicitacao.get(i).getNomeCliente() + "</td>"
								+ "<td>" + solicitacao.get(i).getSolicitante() + "</td>"
								+ "<td>" + solicitacao.get(i).getAfetado() + "</td>"
								+ "<td>" + solicitacao.get(i).getClassificacao() + "</td>"
								+ "<td>" + solicitacao.get(i).getDescricao() + "</td>"
								+ "<td>" + solicitacao.get(i).getPrioridade() + "</td>"
								+ "<td>" + solicitacao.get(i).getNomeFuncionario() + "</td>"
								+ "<td>" + solicitacao.get(i).getStatus() + "<p>"+ solicitacao.get(i).getDataAgendado() +"</p></td>"
							+ "</tr>";
			}
			
			corpoEmail += "</tbody>\n"
					+ "	</table>"
					+ "<br /><br />"
					+ "</body></html>";
			
			 try {
				
				MimeMessage message = emailSender.createMimeMessage();
	            message.setSubject(assunto);
	            MimeMessageHelper helper;
	            helper = new MimeMessageHelper(message, true);
	            helper.setFrom("noreply.providerone@gmail.com");
	            helper.setTo(destinatario);
	            helper.setText(corpoEmail,true);
	            emailSender.send(message);
			 }catch (Exception e) {
		            throw new RuntimeException("Erro ao enviar email!", e);
		     }
		}

		public void enviarEmail(String assunto, String destinatario, String texto) {
			
			try {
				String corpoEmail = "<h4 style='color: red'><b>"+texto+"</b></h3>"; 
				
				MimeMessage message = emailSender.createMimeMessage();
	            message.setSubject(assunto);
	            MimeMessageHelper helper;
	            helper = new MimeMessageHelper(message, true);
	            helper.setFrom("noreply.providerone@gmail.com");
	            helper.setTo(destinatario);
	            helper.setText(corpoEmail,true);
	            emailSender.send(message);
			 }catch (Exception e) {
		            throw new RuntimeException("Erro ao enviar email!", e);
		     }
		}
}
