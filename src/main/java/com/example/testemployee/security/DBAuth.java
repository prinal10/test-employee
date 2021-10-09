//package com.example.testemployee.security;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//public class DBAuth implements AuthenticationProvider {
//
//    @Autowired
//    private UserDAO userDAO;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//
//        User user = userDAO.findByUsername(username);
//        if(user==null) {
//            throw new BadCredentialsException("Username OR Password not found.");
//        } else {
//            if(user.getPassword().equalIgnoreCase(password)) {
//                return new UsernamePasswordAuthenticationToken(username, "", new SimpleGrantedAuthority(user.getRoles()));
//            } else {
//                throw new BadCredentialsException("Password is incorrect.");
//            }
//        }
//
//        return null;
//    }
//
//
//
//    private String getUsername(Authentication authentication) {
//        UsernamePasswordAuthenticationToken token
//                = (UsernamePasswordAuthenticationToken) authentication;
//        if (token.getPrincipal() == null || StringUtils.isEmpty(String.valueOf(token.getPrincipal()))) {
//            throw new BadCredentialsException("Username OR Password not found.");
//        }
//        return String.valueOf(token.getPrincipal());
//    }
//
//    private String getPassword(Authentication authentication) {
//        UsernamePasswordAuthenticationToken token
//                = (UsernamePasswordAuthenticationToken) authentication;
//        if (token.getCredentials() == null || StringUtils.isEmpty(String.valueOf(token.getCredentials()))) {
//            throw new BadCredentialsException("Username OR Password not found.");
//        }
//        return String.valueOf(token.getCredentials());
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return false;
//    }
//}
