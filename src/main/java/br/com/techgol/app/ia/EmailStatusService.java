package br.com.techgol.app.ia;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class EmailStatusService {

    private final AtomicInteger emailsNaoLidos = new AtomicInteger(0);

    public void setQuantidade(int quantidade) {
        emailsNaoLidos.set(quantidade);
    }

    public int getQuantidade() {
        return emailsNaoLidos.get();
    }
}