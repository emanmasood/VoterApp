package com.example.polls.security.accessDecision;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class CustomAccessDecisionVoter implements AccessDecisionVoter<Object> {

    private String anonymousUserName = "anonymous";

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {

        final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        if (StringUtils.equalsAnyIgnoreCase(username, anonymousUserName)) {
            return ACCESS_DENIED;
        }
        return ACCESS_GRANTED;
    }
}