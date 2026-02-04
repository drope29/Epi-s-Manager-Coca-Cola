package com.epis.audit;

import com.epis.entities.Log;
import com.epis.interfaces.AuditAction;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    @Around("@annotation(auditAction)")
    public Object audit(ProceedingJoinPoint joinPoint, AuditAction auditAction) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();

        Object[] args = joinPoint.getArgs();
        String entityId = null;

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(PathVariable.class)) {
                entityId = args[i].toString();
            }
        }

        Object result = joinPoint.proceed();

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Log log = new Log();
        log.setLogId(UUID.randomUUID().toString());
        log.setUsername(username);
        log.setAction(auditAction.action());
        log.setEntity(auditAction.entity());
        log.setEntityId(entityId);
        log.setTimestamp(LocalDateTime.now());

        dynamoDbTemplate.save(log);

        return result;
    }

}
