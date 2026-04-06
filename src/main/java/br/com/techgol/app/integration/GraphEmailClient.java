package br.com.techgol.app.integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microsoft.graph.models.Message;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;

@Service
public class GraphEmailClient {

    @Autowired
    private GraphServiceClient<?> graphClient;

    @Value("${m365.mailbox}")
    private String mailbox;
    
    @Value("${m365.mailboxfolder}")
    private String folder;

    public List<Message> buscarEmailsNaoLidos() {

        MessageCollectionPage page =
                graphClient
                        .users(mailbox)
                        .mailFolders(folder)
                        .messages()
                        .buildRequest()
                        .filter("isRead eq false")
                        .top(100)
                        .orderBy("receivedDateTime desc")
                        .get();

        return page.getCurrentPage();
    }
    
    public void listarPastas() {

        var folders = graphClient
                .users(mailbox)
                .mailFolders()
                .buildRequest()
                .get();

        folders.getCurrentPage().forEach(folder -> {

            System.out.println("Nome: " + folder.displayName);
            System.out.println("ID: " + folder.id);
            System.out.println("-------------------");

        });
    }
    
    public void listarPastasRecursivo(String folderId) {

    	 var folders = graphClient
    	            .users("suporte@providerone.com.br")
    	            .mailFolders()
    	            .buildRequest()
    	            .get();

    	    for (var folder : folders.getCurrentPage()) {

    	        System.out.println("Nome: " + folder.displayName);
    	        System.out.println("ID: " + folder.id);
    	        System.out.println("-----------------------");
    	    }
    }
    
    public void marcarEmailComoProcessado(String messageId) {

        Message update = new Message();
        update.isRead = true;
        update.categories = List.of("Processado");
        System.out.println("MARCANDO EMAIL PROCESSADO!!");

        graphClient
            .users(mailbox)
            .messages(messageId)
            .buildRequest()
            .patch(update);
    }
    
    public int contarEmailsNaoLidos() {

    	var result = graphClient
    		    .users(mailbox)
    		    .mailFolders(folder)
    		    .messages()
    		    .buildRequest()
    		    .filter("isRead eq false")
    		    .count(true)
    		    .get();

    	Long count = result.getCount();
    	int quantidade = count != null ? count.intValue() : 0;

        return quantidade;
    }
    
}
