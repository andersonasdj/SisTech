package br.com.techgol.app.email;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.techgol.app.dto.DtoSolicitacaoComFuncionario;
import br.com.techgol.app.dto.dashboard.DtoDashboard;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.orm.SolicitacaoProjecaoCompleta;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;
import jakarta.mail.internet.MimeMessage;

@Component
public class EnviadorEmail {
	
		@Value("${spring.mail.username}")
		private String senderEmail;
		
		@Value("${sistech.email.copia}")
		private String copiaEmail;
	
		@Autowired
	    private JavaMailSender emailSender;
		
		@Value("${upload.dir}")
		private String UPLOAD_DIR;
		
		@Async
	    public void enviar2fa(String email, String assunto, String mensagem) {
	        try {
	            
	            MimeMessage message = emailSender.createMimeMessage();
	            message.setSubject(assunto);
	            MimeMessageHelper helper;
	            message.setContent(mensagem, "text/html; charset=utf-8");
	            helper = new MimeMessageHelper(message, true);
	            helper.setFrom(senderEmail);
	            helper.setTo(email);
	            helper.setText(mensagem, true);
	            emailSender.send(message);

	            //Simulando demora de 3 segundos para enviar email
	            //Thread.sleep(3000);

	        } catch (Exception e) {
	            throw new RuntimeException("Erro ao enviar email!", e);
	        }
	    }

		@Async
	    public void enviarEmailNovaSolicitacao(DtoSolicitacaoComFuncionario dados, String destinatario) {
	        try {
	            
	            MimeMessage message = emailSender.createMimeMessage();
	            message.setSubject("Cadastro de nova solicitação");
	            MimeMessageHelper helper;
	            helper = new MimeMessageHelper(message, true);
	            helper.setFrom(senderEmail);
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
	            email.setFrom(senderEmail);
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

//		public void enviarEmail(DtoDashboard dto, String assunto, String destinatario, String texto) {
//			var email = new SimpleMailMessage();
//            email.setFrom(senderEmail);
//            email.setSubject(assunto);
//            email.setTo(destinatario);
//            email.setText(texto
//            		+ " Total de Solicitações:  " + dto.totalSolicitacoes() + "\n"
//            		+ "  * ABERTAS: " + dto.aberto() + "\n"
//            		+ "  * AGENDADAS: " + dto.agendado() + "\n"
//            		+ "  * AGUARDANDO: " + dto.aguardando() + "\n"
//            		+ "  * ANDAMENTO: " + dto.andamento() + "\n"
//            		+ "  * PAUSADAS: " + dto.pausado() + "\n"
//            		+ "  \n############################################### \n"
//            		+ " Local das solicitações:  \n"
//            		+ "  * ONSITE: " + dto.onsite() + "\n"
//            		+ "  * OFFSITE: " + dto.offsite() + "\n"
//            		+ "  \n############################################### \n"
//            		+ " Classificação das Solicitações:  \n"
//            		+ "  * INCIDENTE: " + dto.incidente() + "\n"
//            		+ "  * PROBLEMA: " + dto.problema() + "\n"
//            		+ "  * SOLICITAÇÃO: " + dto.solicitacao() + "\n"
//            		+ "  * ACESSO: " + dto.acesso() + "\n"
//            		+ "  * EVENTO: " + dto.evento() + "\n"
//            		+ "  * BACKUP: " + dto.backup() + "\n"
//            		+ "  \n############################################### \n"
//            		+ " Prioridade das Solicitações:  \n"
//            		+ "  * BAIXA: " + dto.baixa() + "\n"
//            		+ "  * MEDIA: " + dto.media() + "\n"
//            		+ "  * ALTA: " + dto.alta() + "\n"
//            		+ "  * CRITICA: " + dto.critica() + "\n"
//            		+ "  * PLANEJADA: " + dto.planejada() + "\n"
//            		
//            		);
//        	emailSender.send(email);
//            System.out.println("Enviando email Relatório diário!");
//            System.out.println("OK");
//			
//		}
		
		public void enviarEmail(DtoDashboard dto, String assunto, String destinatario, String texto) {
		    try {
		        MimeMessage message = emailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		        helper.setFrom(senderEmail);
		        helper.setTo(destinatario);
		        helper.setSubject(assunto);

		        // Conteúdo HTML
		        String conteudoHtml = """
		            <html>
		            <head>
		                <style>
		                    body {
		                        font-family: Arial, sans-serif;
		                        color: #333;
		                        line-height: 1.5;
		                    }
		                    h2 {
		                        color: #0056b3;
		                    }
		                    table {
		                        width: 100%%;
		                        border-collapse: collapse;
		                        margin-top: 10px;
		                        margin-bottom: 20px;
		                    }
		                    table, th, td {
		                        border: 1px solid #ccc;
		                    }
		                    th, td {
		                        padding: 8px;
		                        text-align: left;
		                    }
		                    th {
		                        background-color: #f2f2f2;
		                    }
		                    .section-title {
		                        margin-top: 20px;
		                        font-size: 1.1em;
		                        color: #444;
		                        text-transform: uppercase;
		                    }
		                </style>
		            </head>
		            <body>
		                <h2>Relatório Diário</h2>
		                <p>%s</p>
		                
		                <div class="section-title">Resumo de Solicitações</div>
		                <table>
		                    <tr><th>Total de Solicitações</th><td>%d</td></tr>
		                    <tr><th>Abertas</th><td>%d</td></tr>
		                    <tr><th>Agendadas</th><td>%d</td></tr>
		                    <tr><th>Aguardando</th><td>%d</td></tr>
		                    <tr><th>Andamento</th><td>%d</td></tr>
		                    <tr><th>Pausadas</th><td>%d</td></tr>
		                </table>

		                <div class="section-title">Local das Solicitações</div>
		                <table>
		                    <tr><th>Onsite</th><td>%d</td></tr>
		                    <tr><th>Offsite</th><td>%d</td></tr>
		                </table>

		                <div class="section-title">Classificação</div>
		                <table>
		                    <tr><th>Incidente</th><td>%d</td></tr>
		                    <tr><th>Problema</th><td>%d</td></tr>
		                    <tr><th>Solicitação</th><td>%d</td></tr>
		                    <tr><th>Acesso</th><td>%d</td></tr>
		                    <tr><th>Evento</th><td>%d</td></tr>
		                    <tr><th>Backup</th><td>%d</td></tr>
		                </table>

		                <div class="section-title">Prioridades</div>
		                <table>
		                    <tr><th>Baixa</th><td>%d</td></tr>
		                    <tr><th>Média</th><td>%d</td></tr>
		                    <tr><th>Alta</th><td>%d</td></tr>
		                    <tr><th>Crítica</th><td>%d</td></tr>
		                    <tr><th>Planejada</th><td>%d</td></tr>
		                </table>
		            </body>
		            </html>
		        """.formatted(
		            texto,
		            dto.totalSolicitacoes(),
		            dto.aberto(),
		            dto.agendado(),
		            dto.aguardando(),
		            dto.andamento(),
		            dto.pausado(),
		            dto.onsite(),
		            dto.offsite(),
		            dto.incidente(),
		            dto.problema(),
		            dto.solicitacao(),
		            dto.acesso(),
		            dto.evento(),
		            dto.backup(),
		            dto.baixa(),
		            dto.media(),
		            dto.alta(),
		            dto.critica(),
		            dto.planejada()
		        );

		        helper.setText(conteudoHtml, true);
		        emailSender.send(message);

		        System.out.println("Enviando email Relatório diário!");
		        System.out.println("OK");
		    } catch (Exception e) {
		        e.printStackTrace();
		        System.err.println("Erro ao enviar email: " + e.getMessage());
		    }
		}
		
		public void enviarEmailHostOffline(Solicitacao s, String assunto, String destinatario, String texto) {
		    try {
		        MimeMessage message = emailSender.createMimeMessage();
		        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		        helper.setFrom(senderEmail);
		        helper.setTo(destinatario);
		        helper.setSubject(assunto);

		        String corpoHtml = """
		        <html>
		          <body style="font-family: Arial, sans-serif; color: #333;">
		            <div style="border: 2px solid red; padding: 15px; border-radius: 5px;">
		              <h2 style="color: red;">🚨 ALERTA - HOST OFFLINE</h2>
		              <p style="font-size: 14px;">
		                 %s
		              </p>
		              <p>
		                 <strong>Solicitação ID:</strong> %d<br/>
		                 <strong>Data da solicitação:</strong> %s
		              </p>
		              <p style="color: #555;">
		                 Verifique imediatamente o status do computador e tome as ações necessárias.
		              </p>
		            </div>
		          </body>
		        </html>
		        """.formatted(
		                s.getId(),
		                s.getDataAbertura() != null ? s.getDataAbertura().toString() : "N/A"
		        );

		        helper.setText(corpoHtml, true);

		        emailSender.send(message);

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

		public void enviarEmail(List<SolicitacaoProjecaoEntidadeComAtributos> solicitacao, String assunto, String destinatario, String texto) {
			
//			String corpoEmail = "<html><body>"
//					+ "<style>"
//					+ "table, th, td {\n"
//					+ "  border: 1px solid white;\n"
//					+ "  border-collapse: collapse;\n"
//					+ "  border-spacing: 15px 10px;\n"
//					+ "}\n"
//					+ "table>tbody>tr>td {\n"
//					+ " padding: 5px 20px;\n"
//					+ "	background-color: #FFED8A;\n"
//					+ "}"
//					+ "</style>"
//					+ "<br /><h3> " + texto +" </h3><br /> "
//					+ "<table style=\"text-align: center;\" cellspacing=\"3\" border=\"1\">\n"
//					+ "				<thead>\n"
//					+ "					<tr>\n"
//					+ "						<th scope=\"col\">ID</th>\n"
//					+ "						<th scope=\"col\">Data Abertura</th>\n"
//					+ "						<th scope=\"col\">Cliente</th>\n"
//					+ "						<th scope=\"col\">Solicitante</th>\n"
//					+ "						<th scope=\"col\">Afetado</th>\n"
//					+ "						<th scope=\"col\">Classificações</th>\n"
//					+ "						<th scope=\"col\">Descrição</th>\n"
//					+ "						<th scope=\"col\">Prioridade</th>\n"
//					+ "						<th scope=\"col\">Responsável</th>\n"
//					+ "						<th scope=\"col\">Status</th>\n"
//					+ "					</tr>\n"
//					+ "				</thead>\n"
//					+ "				<tbody>";
			
			String corpoEmail = """
					<html>
					<head>
					    <style>
					        body {
					            font-family: Arial, sans-serif;
					            color: #333;
					            font-size: 14px;
					        }
					        h3 {
					            color: #444;
					            margin-bottom: 10px;
					        }
					        table {
					            width: 100%;
					            border-collapse: collapse;
					            margin-top: 10px;
					        }
					        thead {
					            background-color: #0056b3;
					            color: white;
					        }
					        thead th {
					            padding: 8px 12px;
					            text-align: center;
					        }
					        tbody tr:nth-child(even) {
					            background-color: #f9f9f9;
					        }
					        tbody td {
					            padding: 8px 12px;
					            text-align: center;
					            border-bottom: 1px solid #ddd;
					        }
					        .prioridade-alta {
					            background-color: #ff4d4d;
					            color: white;
					            font-weight: bold;
					        }
					        .prioridade-media {
					            background-color: #ffa64d;
					            color: white;
					        }
					        .prioridade-baixa {
					            background-color: #4caf50;
					            color: white;
					        }
					        .status-aberto {
					            font-weight: bold;
					            color: #0056b3;
					        }
					        .descricao {
					            text-align: left;
					            white-space: pre-line;
					        }
					    </style>
					</head>
					<body>
					    <h3>%s</h3>
					    <table>
					        <thead>
					            <tr>
					                <th>ID</th>
					                <th>Data Abertura</th>
					                <th>Cliente</th>
					                <th>Solicitante</th>
					                <th>Afetado</th>
					                <th>Classificação</th>
					                <th>Descrição</th>
					                <th>Prioridade</th>
					                <th>Responsável</th>
					                <th>Status</th>
					            </tr>
					        </thead>
					        <tbody>
					""".formatted(texto);

//			for(int i=0; i < solicitacao.size(); i++ ) {
//				corpoEmail += "<tr>"
//								+ "<td>" + solicitacao.get(i).getId() + "</td>"
//								+ "<td>" + solicitacao.get(i).getDataAbertura() + "</td>"
//								+ "<td>" + solicitacao.get(i).getNomeCliente() + "</td>"
//								+ "<td>" + solicitacao.get(i).getSolicitante() + "</td>"
//								+ "<td>" + solicitacao.get(i).getAfetado() + "</td>"
//								+ "<td>" + solicitacao.get(i).getClassificacao() + "</td>"
//								+ "<td>" + solicitacao.get(i).getDescricao() + "</td>"
//								+ "<td>" + solicitacao.get(i).getPrioridade() + "</td>"
//								+ "<td>" + solicitacao.get(i).getNomeFuncionario() + "</td>"
//								+ "<td>" + solicitacao.get(i).getStatus() + "<p>"+ solicitacao.get(i).getDataAgendado() +"</p></td>"
//							+ "</tr>";
//			}
//			
//			corpoEmail += "</tbody>\n"
//					+ "	</table>"
//					+ "<br /><br />"
//					+ "</body></html>";
			
			for (SolicitacaoProjecaoEntidadeComAtributos s : solicitacao) {
			    String prioridadeClass = switch (s.getPrioridade().toUpperCase()) {
			        case "ALTA" -> "prioridade-alta";
			        case "MEDIA" -> "prioridade-media";
			        case "BAIXA" -> "prioridade-baixa";
			        default -> "";
			    };

			    corpoEmail += """
			        <tr>
			            <td>%d</td>
			            <td>%s</td>
			            <td>%s</td>
			            <td>%s</td>
			            <td>%s</td>
			            <td>%s</td>
			            <td class="descricao">%s</td>
			            <td class="%s">%s</td>
			            <td>%s</td>
			            <td class="status-aberto">%s<br><small>%s</small></td>
			        </tr>
			    """.formatted(
			        s.getId(),
			        s.getDataAbertura(),
			        s.getNomeCliente(),
			        s.getSolicitante(),
			        s.getAfetado(),
			        s.getClassificacao(),
			        s.getDescricao(),
			        prioridadeClass,
			        s.getPrioridade(),
			        s.getNomeFuncionario(),
			        s.getStatus(),
			        s.getDataAgendado()
			    );
			}
			corpoEmail += "</tbody></table></body></html>";
			
			 try {
				
				MimeMessage message = emailSender.createMimeMessage();
	            message.setSubject(assunto);
	            MimeMessageHelper helper;
	            helper = new MimeMessageHelper(message, true);
	            helper.setFrom(senderEmail);
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
	            helper.setFrom(senderEmail);
	            helper.setTo(destinatario);
	            helper.setText(corpoEmail,true);
	            emailSender.send(message);
			 }catch (Exception e) {
		            throw new RuntimeException("Erro ao enviar email!", e);
		     }
		}
		
		
		public void enviarEmailNotificacao(SolicitacaoProjecaoCompleta solicitacao, String destinatario) {
			String finalizado, duracao, resolucao;
			long horas, min;
			long duracaoTotal;
			
			if(solicitacao.getDataFinalizado() != null) {
				finalizado = solicitacao.getDataFinalizado().toString();
				duracao = solicitacao.getDuracao().toString();
				
				duracaoTotal = Long.parseLong(solicitacao.getDuracao());
				
				if(duracaoTotal <= 60){
					duracao = solicitacao.getDuracao().toString() + " minutos";
				}else{
					min = duracaoTotal % 60;
					horas = (duracaoTotal - min) / 60;
					duracao = horas + "H " + min +"M";
				}
				
			}else { 
				finalizado = "Não finalizado"; 
				duracao = "Não finalizado";
			}
			
			if(solicitacao.getResolucao() != null) {
				resolucao = solicitacao.getResolucao();
			}else {
				resolucao = "";
			}
			
			String corpoEmail = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
					+ " <head>\n"
					+ "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
					+ "  <title>Sistech - Impressão</title>\n"
					+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n"
					+ " </head>\n"
					+ "  <body style=\"margin: 0; padding: 0; background-color:#eaeced \" bgcolor=\"#eaeced\">\n"
					+ "		\n"
					+ "    <!-- Header -->\n"
					+ "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#e1e1e1\">\n"
					+ "      <tr>\n"
					+ "        <td height=\"20\"></td>\n"
					+ "      </tr>\n"
					+ "      <tr>\n"
					+ "        <td>\n"
					+ "          <table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#ffffff\">\n"
					+ "            <tr class=\"hiddenMobile\">\n"
					+ "              <td height=\"40\"> </td>\n"
					+ "            </tr>\n"
					+ "				\n"
					+ "            <tr>\n"
					+ "              <td>\n"
					+ "                <table width=\"480\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullPadding\">\n"
					+ "                  <tbody>\n"
					+ "                    <tr>\n"
					+ "                      <td>\n"
					+ "                        <table width=\"220\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" class=\"col\">\n"
					+ "                          <tbody>\n"
					+ "                            <tr>\n"
					+ "                              <td align=\"left\"><br> <span id=\"nomeCliente\" style=\"font-size: 22px; color: #5b5b5b; font-family: 'Open Sans', sans-serif;\" > " + solicitacao.getNomeCliente() + "</span>  </td>\n"
					+ "                            </tr>\n"
					+ "                            <tr class=\"hiddenMobile\">\n"
					+ "                              <td height=\"50\"></td>\n"
					+ "                            </tr>\n"
					+ "                            <tr>\n"
					+ "                              <td style=\"font-size: 12px; color: #5b5b5b; font-family: 'Open Sans', sans-serif; line-height: 18px; vertical-align: top; text-align: left;\">\n"
					+ "                                Solicitação de atendimento\n"
					+ "                                <br>Data de abertura: <span id=\"dataAbertura\">" + solicitacao.getDataAbertura() + " </span>\n"
					+ "                                <br>Forma de Abertura: <span id=\"formaAbertura\">" + solicitacao.getFormaAbertura() + "</span>\n"
					+ "                                <br>Local: <span id=\"local\">" + solicitacao.getLocal() + "</span>\n"
					+ "                              </td>\n"
					+ "                            </tr>\n"
					+ "                          </tbody>\n"
					+ "                        </table>\n"
					+ "                        <table width=\"220\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"right\" class=\"col\">\n"
					+ "                          <tbody>\n"
					+ "                            <tr class=\"visibleMobile\">\n"
					+ "                              <td height=\"20\"> </td>\n"
					+ "                            </tr>\n"
					+ "                            <tr>\n"
					+ "                              <td height=\"5\"> </td>\n"
					+ "                            </tr>\n"
					+ "                            <tr>\n"
					+ "                              <td style=\"font-size: 21px; color: rgb(28, 113, 216); letter-spacing: -1px; font-family: 'Open Sans', sans-serif; line-height: 1; padding-right: 2px; vertical-align: top; text-align: right;\">\n"
					+ "                                Ordem de serviço\n"
					+ "                              </td>\n"
					+ "                            </tr>\n"
					+ "                            <tr>\n"
					+ "                            <tr class=\"hiddenMobile\">\n"
					+ "                              <td height=\"30\"> </td>\n"
					+ "                            </tr>\n"
					+ "                            <tr class=\"visibleMobile\">\n"
					+ "                              <td height=\"20\"> </td>\n"
					+ "                            </tr>\n"
					+ "                            <tr>\n"
					+ "                              <td style=\"font-size: 12px; color: rgb(0, 0, 0); font-family: 'Open Sans', sans-serif; line-height: 18px; vertical-align: top; text-align: right; padding-right: 2px;\">\n"
					+ "                                <small>ID</small> #<span id=\"idSolicitacao\"> " + solicitacao.getId() + " </span><br />\n"
					+ "                                <small id=\"dataHoje\"> </small>\n"
					+ "                              </td>\n"
					+ "                            </tr>\n"
					+ "                          </tbody>\n"
					+ "                        </table>\n"
					+ "                      </td>\n"
					+ "                    </tr>\n"
					+ "                  </tbody>\n"
					+ "                </table>\n"
					+ "              </td>\n"
					+ "            </tr>\n"
					+ "          </table>\n"
					+ "        </td>\n"
					+ "      </tr>\n"
					+ "    </table>\n"
					+ "    <!-- /Header -->\n"
					+ "    <!-- Order Details -->\n"
					+ "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#e1e1e1\">\n"
					+ "      <tbody>\n"
					+ "        <tr>\n"
					+ "          <td>\n"
					+ "            <table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#ffffff\">\n"
					+ "              <tbody>\n"
					+ "                <tr>\n"
					+ "                <tr class=\"visibleMobile\">\n"
					+ "                  <td height=\"50\"> </td>\n"
					+ "                </tr>\n"
					+ "                <tr>\n"
					+ "                  <td>\n"
					+ "                    <table width=\"480\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullPadding\">\n"
					+ "                      <tbody>\n"
					+ "                        <tr>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 10px 7px 0;\" width=\"52%\" align=\"left\">\n"
					+ "                            Descrição\n"
					+ "                          </th>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 0 7px;\" align=\"left\">\n"
					+ "                            <small> </small>\n"
					+ "                          </th>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 0 7px;\" align=\"center\">\n"
					+ "                            Solicitante\n"
					+ "                          </th>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 0 7px;\" align=\"right\">\n"
					+ "                            Afetado\n"
					+ "                          </th>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"1\" style=\"background: #bebebe;\" colspan=\"4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"10\" colspan=\"4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: rgb(28, 113, 216);  line-height: 18px;  vertical-align: top; padding:10px 0;\" class=\"article\">\n"
					+ "                            <b><span id=\"descricao\"> " + solicitacao.getDescricao() + " </span></b>\n"
					+ "                          </td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e;  line-height: 18px;  vertical-align: top; padding:10px 0;\"><small></small></td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e;  line-height: 18px;  vertical-align: top; padding:10px 0;\" align=\"center\"><b><span id=\"solicitante\"> " + solicitacao.getSolicitante() + " </span></b> </td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b;  line-height: 18px;  vertical-align: top; padding:10px 0;\" align=\"right\"><b><span id=\"afetado\"> " + solicitacao.getAfetado() + " </span></b></td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"1\" colspan=\"4\" style=\"border-bottom:1px solid #e4e4e4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                      </tbody>\n"
					+ "                    </table>\n"
					+ "                  </td>\n"
					+ "                </tr>\n"
					+ "                <tr>\n"
					+ "                  <td height=\"20\"> </td>\n"
					+ "                </tr>\n"
					+ "              </tbody>\n"
					+ "            </table>\n"
					+ "          </td>\n"
					+ "        </tr>\n"
					+ "      </tbody>\n"
					+ "    </table>\n"
					+ "     <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#e1e1e1\">\n"
					+ "      <tbody>\n"
					+ "        <tr>\n"
					+ "          <td>\n"
					+ "            <table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#ffffff\">\n"
					+ "              <tbody>\n"
					+ "                <tr>\n"
					+ "                  <td>\n"
					+ "                    <table width=\"480\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullPadding\">\n"
					+ "                      <tbody>\n"
					+ "                        <tr>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 10px 7px 0;\" width=\"52%\" align=\"left\">\n"
					+ "                            Observação\n"
					+ "                          </th>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 0 7px;\" align=\"left\">\n"
					+ "                            <small> </small>\n"
					+ "                          </th>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 0 7px;\" align=\"center\">\n"
					+ "                            Classificação\n"
					+ "                          </th>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 0 7px;\" align=\"right\">\n"
					+ "                            Categoria\n"
					+ "                          </th>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"1\" style=\"background: #bebebe;\" colspan=\"4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"10\" colspan=\"4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: rgb(28, 113, 216);  line-height: 18px;  vertical-align: top; padding:10px 0;\" class=\"article\">\n"
					+ "                            <b><span id=\"observacao\"> </span></b>\n"
					+ "                          </td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e;  line-height: 18px;  vertical-align: top; padding:10px 0;\"><small></small></td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e;  line-height: 18px;  vertical-align: top; padding:10px 0;\" align=\"center\"><b><span id=\"classificacao\"> " + solicitacao.getClassificacao() + " </span></b></td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b;  line-height: 18px;  vertical-align: top; padding:10px 0;\" align=\"right\"><b><span id=\"categoria\"> " + solicitacao.getCategoria() + " </span></b></td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"1\" colspan=\"4\" style=\"border-bottom:1px solid #e4e4e4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                      </tbody>\n"
					+ "                    </table>\n"
					+ "                  </td>\n"
					+ "                </tr>\n"
					+ "                <tr>\n"
					+ "                  <td height=\"20\"> </td>\n"
					+ "                </tr>\n"
					+ "              </tbody>\n"
					+ "            </table>\n"
					+ "          </td>\n"
					+ "        </tr>\n"
					+ "      </tbody>\n"
					+ "    </table>\n"
					+ "    \n"
					+ "      <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#e1e1e1\">\n"
					+ "      <tbody>\n"
					+ "        <tr>\n"
					+ "          <td>\n"
					+ "            <table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#ffffff\">\n"
					+ "              <tbody>\n"
					+ "                <tr>\n"
					+ "                  <td>\n"
					+ "                    <table width=\"480\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullPadding\">\n"
					+ "                      <tbody>\n"
					+ "                        <tr>\n"
					+ "                          <th style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; font-weight: normal; line-height: 1; vertical-align: top; padding: 0 10px 7px 0;\" width=\"52%\" align=\"left\">Resolução\n"
					+ "                          </th>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"1\" style=\"background: #bebebe;\" colspan=\"4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"10\" colspan=\"4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: rgb(28, 113, 216);  line-height: 18px;  vertical-align: top; padding:10px 0;\" class=\"article\">\n"
					+ "                            <b><span id=\"resolucao\"> " + resolucao + " </span></b>\n"
					+ "                          </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td height=\"1\" colspan=\"4\" style=\"border-bottom:1px solid #e4e4e4\"> </td>\n"
					+ "                        </tr>\n"
					+ "                      </tbody>\n"
					+ "                    </table>\n"
					+ "                  </td>\n"
					+ "                </tr>\n"
					+ "                <tr>\n"
					+ "                  <td height=\"20\"> </td>\n"
					+ "                </tr>\n"
					+ "              </tbody>\n"
					+ "            </table>\n"
					+ "          </td>\n"
					+ "        </tr>\n"
					+ "      </tbody>\n"
					+ "    </table>\n"
					+ "    \n"
					+ "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#e1e1e1\">\n"
					+ "      <tbody>\n"
					+ "        <tr>\n"
					+ "          <td>\n"
					+ "            <table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#ffffff\">\n"
					+ "              <tbody>\n"
					+ "                <tr>\n"
					+ "                  <td>\n"
					+ "                    <!-- Table Total -->\n"
					+ "                    <table width=\"480\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullPadding\">\n"
					+ "                      <tbody>\n"
					+ "						   <tr>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e; line-height: 22px; vertical-align: top; text-align:right; \">\n"
					+ "                            Data de conclusão\n"
					+ "                          </td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e; line-height: 22px; vertical-align: top; text-align:right; white-space:nowrap;\" width=\"150\">\n"
					+ "                            <span id=\"dataFinalizado\"> " + finalizado + " </span>\n"
					+ "                          </td>\n"
					+ "                        </tr>\n"
					+ "						 <tr>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e; line-height: 22px; vertical-align: top; text-align:right; \">\n"
					+ "                            Status\n"
					+ "                          </td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e; line-height: 22px; vertical-align: top; text-align:right; white-space:nowrap;\" width=\"150\">\n"
					+ "                            <span id=\"status\"> " + solicitacao.getStatus() + " </span>\n"
					+ "                          </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e; line-height: 22px; vertical-align: top; text-align:right; \">\n"
					+ "                            Analista responsável\n"
					+ "                          </td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #646a6e; line-height: 22px; vertical-align: top; text-align:right; white-space:nowrap;\" width=\"150\">\n"
					+ "                            <span id=\"nomeFuncionario\"> " + solicitacao.getNomeFuncionario() + "</span>\n"
					+ "                          </td>\n"
					+ "                        </tr>\n"
					+ "                        <tr>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #000; line-height: 22px; vertical-align: top; text-align:right; \">\n"
					+ "                            <strong>Tempo de atendimento</strong>\n"
					+ "                          </td>\n"
					+ "                          <td style=\"font-size: 12px; font-family: 'Open Sans', sans-serif; color: #000; line-height: 22px; vertical-align: top; text-align:right; \">\n"
					+ "                            <strong><span id=\"duracao\"> " + duracao + " </span></strong>\n"
					+ "                          </td>\n"
					+ "                        </tr>\n"
					+ "                      </tbody>\n"
					+ "                    </table>\n"
					+ "                    <!-- /Table Total -->\n"
					+ "						\n"
					+ "                  </td>\n"
					+ "                </tr>\n"
					+ "              </tbody>\n"
					+ "            </table>\n"
					+ "          </td>\n"
					+ "        </tr>\n"
					+ "      </tbody>\n"
					+ "    </table>\n"
					+ "    <!-- /Total -->\n"
					+ "    <!-- Information -->\n"
					+ "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#e1e1e1\">\n"
					+ "      <tbody>\n"
					+ "        <tr>\n"
					+ "          <td>\n"
					+ "            <table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#ffffff\">\n"
					+ "              <tbody>\n"
					+ "                <tr>\n"
					+ "                  <td>\n"
					+ "                    <table width=\"480\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullPadding\">\n"
					+ "                      <tbody>\n"
					+ "                        <tr>\n"
					+ "                          <td>\n"
					+ "                            <table width=\"220\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" class=\"col\">\n"
					+ "                              <tbody>\n"
					+ "                                <tr class=\"hiddenMobile\">\n"
					+ "                                  <td height=\"85\"> </td>\n"
					+ "                                </tr>\n"
					+ "                                <tr class=\"visibleMobile\">\n"
					+ "                                  <td height=\"20\"> </td>\n"
					+ "                                </tr>\n"
					+ "                                 <tr>\n"
					+ "                                  <td style=\"font-size: 11px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; line-height: 1; vertical-align: top; \">\n"
					+ "                                  </td>\n"
					+ "                                </tr>\n"
					+ "                                <tr>\n"
					+ "                                  <td style=\"font-size: 11px; padding-left: 20px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; line-height: 1; vertical-align: top; \">\n"
					+ "                                    <strong></strong>\n"
					+ "                                  </td>\n"
					+ "                                </tr>\n"
					+ "                                <tr>\n"
					+ "                                  <td style=\"font-size: 11px; padding-top: 5px; padding-left: 68px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; line-height: 1; vertical-align: top; \">\n"
					+ "                                    <strong></strong>\n"
					+ "                                  </td>\n"
					+ "                                </tr>\n"
					+ "                                <tr>\n"
					+ "                                  <td width=\"100%\" height=\"10\"> </td>\n"
					+ "                                </tr>\n"
					+ "                              </tbody>\n"
					+ "                            </table>\n"
					+ "								\n"
					+ "                            <table width=\"220\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"right\" class=\"col\">\n"
					+ "                              <tbody>\n"
					+ "                                <tr class=\"hiddenMobile\">\n"
					+ "                                  <td height=\"85\"> </td>\n"
					+ "                                </tr>\n"
					+ "                                <tr class=\"visibleMobile\">\n"
					+ "                                  <td height=\"20\"> </td>\n"
					+ "                                </tr>\n"
					+ "                                 <tr>\n"
					+ "                                  <td style=\"font-size: 11px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; line-height: 1; vertical-align: top; \">\n"
					+ "                                  </td>\n"
					+ "                                </tr>\n"
					+ "                                <tr>\n"
					+ "                                  <td style=\"font-size: 11px; padding-left: 20px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; line-height: 1; vertical-align: top; \">\n"
					+ "                                    <strong></strong>\n"
					+ "                                  </td>\n"
					+ "                                </tr>\n"
					+ "                                <tr>\n"
					+ "                                  <td style=\"font-size: 11px; padding-top: 5px; padding-left: 50px; font-family: 'Open Sans', sans-serif; color: #5b5b5b; line-height: 1; vertical-align: top; \">\n"
					+ "                                    <strong></strong>\n"
					+ "                                  </td>\n"
					+ "                                </tr>\n"
					+ "                                <tr>\n"
					+ "                                  <td width=\"100%\" height=\"10\"> </td>\n"
					+ "                                </tr>\n"
					+ "                              </tbody>\n"
					+ "                            </table>\n"
					+ "                          </td>\n"
					+ "                        </tr>\n"
					+ "                      </tbody>\n"
					+ "                    </table>\n"
					+ "                  </td>\n"
					+ "                </tr>\n"
					+ "                <tr class=\"visibleMobile\">\n"
					+ "                  <td height=\"30\"> </td>\n"
					+ "                </tr>\n"
					+ "              </tbody>\n"
					+ "            </table>\n"
					+ "          </td>\n"
					+ "        </tr>\n"
					+ "      </tbody>\n"
					+ "    </table>\n"
					+ "    <!-- /Information -->\n"
					+ "    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#e1e1e1\">\n"
					+ "      <tr>\n"
					+ "        <td>\n"
					+ "          <table width=\"600\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullTable\" bgcolor=\"#ffffff\" style=\"border-radius: 0 0 10px 10px;\">\n"
					+ "            <tr>\n"
					+ "              <td>\n"
					+ "                <table width=\"480\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" class=\"fullPadding\">\n"
					+ "                  <tbody>\n"
					+ "					 <tr>\n"
					+ "                      <td style=\"font-size: 12px; color: #5b5b5b; font-family: 'Open Sans', sans-serif; line-height: 18px; vertical-align: top; text-align: left;\">\n"
					+ "						   <td height=\"35\"> </td>\n"
					+ "                      </td>\n"
					+ "                    </tr>\n"
					+ "                    <tr>\n"
					+ "                      <td style=\"font-size: 12px; color: #5b5b5b; font-family: 'Open Sans', sans-serif; line-height: 18px; vertical-align: top; text-align: left;\">\n"
					+ "						   <td height=\"35\"> </td>\n"
					+ "                      </td>\n"
					+ "                    </tr>\n"
					+ "                    <tr>\n"
					+ "                      <td style=\"font-size: 9px; color: #5b5b5b; font-family: 'Open Sans', sans-serif; line-height: 18px; vertical-align: top; text-align: left;\">\n"
					+ "                        Tecnologia da Informação\n"
					+ "                      </td>\n"
					+ "                    </tr>\n"
					+ "                  </tbody>\n"
					+ "                </table>\n"
					+ "              </td>\n"
					+ "            </tr>\n"
					+ "            <tr class=\"spacer\">\n"
					+ "              <td height=\"50\"> </td>\n"
					+ "            </tr>\n"
					+ "          </table>\n"
					+ "        </td>\n"
					+ "      </tr>\n"
					+ "      <tr>\n"
					+ "        <td height=\"20\"> </td>\n"
					+ "      </tr>\n"
					+ "    </table>\n"
					+ "    \n"
					+ "</body>";
			
			 try {
				MimeMessage message = emailSender.createMimeMessage();
	            message.setSubject("Solicitação de atendimento - " + solicitacao.getId());
	            MimeMessageHelper helper;
	            helper = new MimeMessageHelper(message, true);
	            helper.setFrom(senderEmail);
	            if(copiaEmail != null) {
					helper.setCc(copiaEmail);
				}
	            helper.setTo(destinatario);
	            helper.setText(corpoEmail,true);
	            
	            // Caminho do anexo
	            String caminhoImagem = UPLOAD_DIR + solicitacao.getAnexo();
	            File arquivo = new File(caminhoImagem);
	            
	            if (arquivo.exists()) {
	                helper.addAttachment(arquivo.getName(), arquivo);
	            }
	            
	            emailSender.send(message);
			 }catch (Exception e) {
		            throw new RuntimeException("Erro ao enviar email!", e);
		     }
		}
}
