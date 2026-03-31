package br.com.techgol.app.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import okhttp3.Request;
import com.microsoft.graph.requests.GraphServiceClient;

@Configuration
public class GraphConfig {

    @Value("${m365.client-id}")
    private String clientId;

    @Value("${m365.client-secret}")
    private String clientSecret;

    @Value("${m365.tenant-id}")
    private String tenantId;

    @Bean
    public GraphServiceClient<Request> graphClient() {

        ClientSecretCredential credential =
                new ClientSecretCredentialBuilder()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .tenantId(tenantId)
                        .build();

        TokenCredentialAuthProvider authProvider =
                new TokenCredentialAuthProvider(
                        List.of("https://graph.microsoft.com/.default"),
                        credential
                );

        return GraphServiceClient
                .builder()
                .authenticationProvider(authProvider)
                .buildClient();
    }
}
