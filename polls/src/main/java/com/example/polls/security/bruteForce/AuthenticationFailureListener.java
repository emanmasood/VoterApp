package com.example.polls.security.bruteForce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional("defaultTransactionManager")
public class AuthenticationFailureListener implements ApplicationListener < AuthenticationFailureBadCredentialsEvent > {

    private static Logger LOG = LoggerFactory.getLogger(AuthenticationFailureListener.class);

    @Resource(name = "bruteForceProtectionService")
    private BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        LOG.info("*********  Login failed for user {} ", username);
        bruteForceProtectionService.registerLoginFailure(username);

    }

}
