package be.wouterversyck.shoppinglistapi.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginAudit {

    @EventListener
    public void auditEventHappened(AuditApplicationEvent auditApplicationEvent) {

        AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
        log.info("Principal {} - {}", auditEvent.getPrincipal(), auditEvent.getType());
    }
}
