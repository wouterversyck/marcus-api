package be.wouterversyck.shoppinglistapi.security.utils;

import be.wouterversyck.shoppinglistapi.security.filters.JwtAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoginAudit {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    @EventListener
    public void auditEventHappened(AuditApplicationEvent auditApplicationEvent) {

        AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
        log.info("Principal {} - {}", auditEvent.getPrincipal(), auditEvent.getType());
    }
}
