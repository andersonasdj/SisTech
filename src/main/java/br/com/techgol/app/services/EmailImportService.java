package br.com.techgol.app.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.microsoft.graph.models.Message;

import br.com.techgol.app.ia.EmailStatusService;
import br.com.techgol.app.ia.openai.OpenAIService;
import br.com.techgol.app.integration.GraphEmailClient;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.EmailProcessado;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.repository.EmailProcessadoRepository;
import br.com.techgol.app.repository.FuncionarioRepository;
import br.com.techgol.app.repository.SolicitacaoRepository;

@Service
@ConditionalOnProperty(
        name = "automation.email.enabled",
        havingValue = "true"
)
public class EmailImportService {

    @Autowired private GraphEmailClient emailClient;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private SolicitacaoRepository solicitacaoRepository;
    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private EmailProcessadoRepository emailRepository;
    //@Autowired AISuggestionService aiSuggestionService;
    @Autowired OpenAIService openAIService;
    @Autowired private EmailStatusService emailStatusService;
    //@Autowired private Executor emailExecutor; // Em teste

    public void processarEmails() {

        List<Message> emails = emailClient.buscarEmailsNaoLidos();
        Funcionario funcionario = funcionarioRepository.findBynomeFuncionario("Suporte");
        boolean iaDisponivel = openAIService.isDisponivel();
        int quantidade = emailClient.contarEmailsNaoLidos();
        emailStatusService.setQuantidade(quantidade);

        System.out.println("IA Disponivel? :" + iaDisponivel);
        System.out.println("Quantidade emails não lidos : " + emails.size());
        for (Message email : emails) {
        	System.out.println("\n\n * Remetente do email: " + email.from.emailAddress.address);
        	System.out.println(" * Assunto do email: " + email.subject);
        	processarEmail(email, funcionario, iaDisponivel);
//        	emailExecutor.execute(() -> {
//                processarEmail(email, funcionario, iaDisponivel);
//            });
        }
    }
    
    public void listaPastas() {
    	//emailClient.listarPastas();
    	emailClient.listarPastasRecursivo("inbox");
    }
    
    public String extrairDominio(String email) {

        if(email == null)
            return null;

        return email.substring(email.indexOf("@") + 1).toLowerCase();
    }
    
    private void registrarEmailProcessado(String messageId) {
        EmailProcessado proc = new EmailProcessado();
        proc.setMessageId(messageId);
        proc.setDataProcessamento(LocalDateTime.now());
        emailRepository.save(proc);
    }
    
    private void processarEmail(Message email, Funcionario funcionario, boolean iaDisponivel) {
    	
    	if(emailRepository.existsById(email.id))
    	    return;

    	if(email.from == null || email.from.emailAddress == null)
    	    return;

    	String idConversa = email.conversationId != null ? email.conversationId : email.id;

    	if(solicitacaoRepository.existsByConversationId(idConversa)) {
    	    registrarEmailProcessado(email.id);
    	   // emailClient.marcarEmailComoProcessado(idConversa);
    	    return;
    	}

    	String remetente = email.from.emailAddress.address;
    	String dominio = extrairDominio(remetente);

    	Optional<Cliente> clienteOpt = clienteRepository.findByDominioIgnoreCase(dominio);

    	if(clienteOpt.isEmpty())
    	    return;

    	Cliente cliente = clienteOpt.get();
    	String corpo = "";

    	if(email.body != null && email.body.content != null) {
    	    corpo = Jsoup.parse(email.body.content).text();
    	}

    	corpo = corpo
    	    .replaceAll("\\s+", " ")
    	    .trim()
    	    .replaceAll("(?im)^att\\..*$", "")
    	    .replaceAll("(?im)^from:.*$", "")
    	    .replaceAll("(?im)^sent:.*$", "")
    	    .replaceAll("(?im)^subject:.*$", "")
    	    .split("(?i)-----original message-----")[0]
    	    .trim();

    	if(corpo.length() > 1000) {
    	    corpo = corpo.substring(0, 1000);
    	}

    	String assunto = email.subject != null ? email.subject : "(sem assunto)";
    	assunto = assunto
    		    .replaceAll("(?i)^re:\\s*", "")
    		    .replaceAll("(?i)^fw:\\s*", "")
    		    .replaceAll("(?i)^enc:\\s*", "")
    		    .trim();
    	
    	String descricao;

    	String corpoLower = corpo.toLowerCase();
    	String assuntoLower = assunto.toLowerCase();

    	if(corpoLower.contains(assuntoLower)) {
    	    descricao = corpo;
    	} else {
    	    descricao = assunto + " - " + corpo;
    	}

    	descricao = descricao.substring(0, Math.min(descricao.length(), 350));
    	LocalDateTime agora = LocalDateTime.now().withNano(0);

    	Solicitacao solicitacao = new Solicitacao();
    	solicitacao.setDescricao(descricao);
        solicitacao.setCliente(cliente);
        solicitacao.setFuncionario(funcionario);
        solicitacao.setFormaAbertura(FormaAbertura.EMAIL);
        solicitacao.setStatus(Status.TRIAGEM);
        solicitacao.setDataAbertura(agora);
        solicitacao.setDataAtualizacao(agora);
        solicitacao.setSolicitante(remetente);
        solicitacao.setAfetado(remetente);
        solicitacao.setExcluido(false);
        solicitacao.setCategoria(Categoria.OUTROS);
        solicitacao.setAbertoPor("Sistema");
        solicitacao.setClassificacao(Classificacao.SOLICITACAO);
        solicitacao.setPrioridade(Prioridade.ALTA);
        solicitacao.setLocal(Local.OFFSITE);
        solicitacao.setVersao(0);
        solicitacao.setConversationId(idConversa);
        solicitacao.setDuracao(0l);
        solicitacao.setPeso(0l);

    	solicitacaoRepository.save(solicitacao);
    	registrarEmailProcessado(email.id);
    	
    	// PROCESSAMENTO DA IA
    	if(iaDisponivel && corpo.length() > 200) {
    		openAIService.resumirEmail(solicitacao.getId(), assunto, corpo);
    	}
    	

    }
    
}	
    	/* PROCESSAMENTO DA IA
    	if(iaDisponivel && corpo.length() > 200) {
    	    aiSuggestionService.processarResumoAsync(solicitacao.getId(), assunto, corpo);
    	}
    	*/
    	
    	
    	
//    	 String idConversa = email.conversationId != null ? email.conversationId : email.id;
//    	 
//    	 Optional<Solicitacao> solicitacaoExistente = solicitacaoRepository.findByConversationId(idConversa);
//
//    		if(solicitacaoExistente.isPresent()) {
//    		    registrarEmailProcessado(email.id);
//    		    return;
//    		}
//
//        if(emailRepository.existsById(email.id))
//            return;
//
//        if(email.from == null || email.from.emailAddress == null)
//            return;
//
//        String remetente = email.from.emailAddress.address;
//        String dominio = extrairDominio(remetente);
//
//        Optional<Cliente> clienteOpt = clienteRepository.findByDominioIgnoreCase(dominio);
//
//        if(clienteOpt.isEmpty())
//            return;
//
//        Cliente cliente = clienteOpt.get();
//
//        String corpo = "";
//
//        if(email.body != null && email.body.content != null) {
//            corpo = Jsoup.parse(email.body.content).text();
//        }
//
//        corpo = corpo.replaceAll("\\s+", " ").trim();
//        
//        String assunto = email.subject != null ? email.subject : "(sem assunto)";
//
//        String descricao = assunto + " - " + corpo;
//        descricao = descricao.substring(0, Math.min(descricao.length(), 350));
//
//        LocalDateTime agora = LocalDateTime.now().withNano(0);
//        
//        Solicitacao solicitacao = new Solicitacao();
//        solicitacao.setDescricao(descricao);
//        solicitacao.setCliente(cliente);
//        solicitacao.setFuncionario(funcionario);
//        solicitacao.setFormaAbertura(FormaAbertura.EMAIL);
//        solicitacao.setStatus(Status.ABERTO);
//        solicitacao.setDataAbertura(agora);
//        solicitacao.setDataAtualizacao(agora);
//        solicitacao.setSolicitante(remetente);
//        solicitacao.setAfetado(remetente);
//        solicitacao.setExcluido(false);
//        solicitacao.setCategoria(Categoria.OUTROS);
//        solicitacao.setAbertoPor("Sistema");
//        solicitacao.setClassificacao(Classificacao.SOLICITACAO);
//        solicitacao.setPrioridade(Prioridade.ALTA);
//        solicitacao.setLocal(Local.OFFSITE);
//        solicitacao.setVersao(0);
//        solicitacao.setConversationId(idConversa);
//        solicitacao.setDuracao(0l);
//        solicitacao.setPeso(0l);
//        
//        solicitacaoRepository.save(solicitacao);
//
//        registrarEmailProcessado(email.id);
//
//        if(iaDisponivel) {
//        	System.out.println(" IA disponivel \n");
//            aiSuggestionService.processarResumoAsync(solicitacao.getId(), assunto, corpo);
//        }
