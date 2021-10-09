package com.example.testemployee.security;

import com.example.testemployee.properties.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class AuthorityAuthenticationProvider implements AuthenticationProvider {

    private final ApplicationProperties applicationProperties;

    public AuthorityAuthenticationProvider(
            ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = getUsername(authentication);
        String password = getPassword(authentication);
        List<SimpleGrantedAuthority> simpleGrantedAuthorities;
        if (verifyCronCredentials(username, password)) {
            simpleGrantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority("CRON_ROLE"));
        } else {
            simpleGrantedAuthorities = verifyCredentialAndGetAuthority(username, password);
        }
        return getAuthenticationForUserWithAuthorities(username, simpleGrantedAuthorities);
    }

    private String getUsername(Authentication authentication) {
        UsernamePasswordAuthenticationToken token
                = (UsernamePasswordAuthenticationToken) authentication;
        if (token.getPrincipal() == null || StringUtils.isEmpty(String.valueOf(token.getPrincipal()))) {
            throw new BadCredentialsException("Username OR Password not found.");
        }
        return String.valueOf(token.getPrincipal());
    }

    private String getPassword(Authentication authentication) {
        UsernamePasswordAuthenticationToken token
                = (UsernamePasswordAuthenticationToken) authentication;
        if (token.getCredentials() == null || StringUtils.isEmpty(String.valueOf(token.getCredentials()))) {
            throw new BadCredentialsException("Username OR Password not found.");
        }
        return String.valueOf(token.getCredentials());
    }

    private Authentication getAuthenticationForUserWithAuthorities(String username,
                                                                   List<SimpleGrantedAuthority> simpleGrantedAuthorities) {
        if (!CollectionUtils.isEmpty(simpleGrantedAuthorities)) {
            log.debug("successfully authenticated cron user with username: " + username);
            return new UsernamePasswordAuthenticationToken(username, "", simpleGrantedAuthorities);
        }
        log.debug("cannot authenticate user with username: " + username);
        throw new BadCredentialsException("Invalid Username OR Password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);

    }

    private boolean verifyCronCredentials(String username, String password) {
        String cronUsername = applicationProperties.getCronUsername();
        String cronPassword = applicationProperties.getCronPassword();
        return (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(cronUsername) &&
                cronUsername.equals(username)
                && StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(cronPassword) &&
                cronPassword.equals(password));
    }

    private List<SimpleGrantedAuthority> verifyCredentialAndGetAuthority(String username, String password) {
        if (applicationProperties.getAdminUsername().equalsIgnoreCase(username) &&
                applicationProperties.getAdminPassword().equalsIgnoreCase(password)) {
            return Collections.singletonList(new SimpleGrantedAuthority("READ_WRITE"));
        } else if (applicationProperties.getPublicUsername().equalsIgnoreCase(username) &&
                applicationProperties.getPublicPassword().equalsIgnoreCase(password)) {
            return Collections.singletonList(new SimpleGrantedAuthority("READ_ONLY"));
        }
        return null;
    }
}
