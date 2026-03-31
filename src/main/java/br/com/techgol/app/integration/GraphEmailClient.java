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
                        .mailFolders("AAMkADU0ODY3MzBjLTk4YjUtNGYwNi05YTEzLTA3NTM1Y2MzMmJjYQAuAAAAAAASezO-YHz9TqhrjBLIUwdEAQDiAIpPvvtuTJwXmzea1JHQAAqyPGdDAAA=")
                        .messages()
                        .buildRequest()
                        .filter("isRead eq false")
                        .top(30)
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
    
}
