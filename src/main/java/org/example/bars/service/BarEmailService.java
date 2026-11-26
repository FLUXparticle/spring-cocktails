package org.example.bars.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class BarEmailService {
    private static final Logger LOG = LoggerFactory.getLogger(BarEmailService.class);

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendEmail(BarChanged event) throws InterruptedException {
        Thread.sleep(2000);
        LOG.info("Simulierter E-Mail-Versand für Bar {} an {}", event.id(), event.email());
    }
}
