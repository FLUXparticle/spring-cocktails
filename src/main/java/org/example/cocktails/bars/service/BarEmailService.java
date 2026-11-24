package org.example.cocktails.bars.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.event.*;

@Service
public class BarEmailService {
    private static final Logger LOG = LoggerFactory.getLogger(BarEmailService.class);
    private final long delayMillis;

    public BarEmailService(@Value("${bars.email.delay-ms:2000}") long delayMillis) {
        this.delayMillis = Math.max(0, delayMillis);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEmail(BarChanged event) {
        // Course simulation, no mail is sent to external recipients.
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        }
        LOG.info("Simulierter E-Mail-Versand für Bar {} an {}", event.barId(), event.email());
    }
}
