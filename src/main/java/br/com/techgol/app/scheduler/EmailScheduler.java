package br.com.techgol.app.scheduler;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.techgol.app.services.EmailImportService;

@Component
@EnableScheduling
@ConditionalOnProperty(
        name = "automation.email.enabled",
        havingValue = "true"
)
public class EmailScheduler {

    @Autowired
    private EmailImportService emailImportService;

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Scheduled(fixedDelayString = "${automation.email.interval:60000}")
    public void verificarEmails() {

        if(!running.compareAndSet(false, true))
            return;

        try {
            emailImportService.processarEmails();
        } finally {
            running.set(false);
        }
    }
}
