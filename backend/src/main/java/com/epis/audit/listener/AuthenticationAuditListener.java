package com.epis.audit.listener;

import com.epis.entities.Log;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AuthenticationAuditListener {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {

        String username = event.getAuthentication().getName();

        Log log = new Log();
        log.setLogId(UUID.randomUUID().toString());
        log.setUsername(username);
        log.setAction("LOGIN");
        log.setEntity(null);
        log.setEntityId(null);
        log.setTimestamp(LocalDateTime.now());

        dynamoDbTemplate.save(log);

    }

}
