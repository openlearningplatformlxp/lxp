package org.springframework.security.saml.web;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

public class SAMLDetails implements SAMLUserDetailsService {
    @Override
    public Object loadUserBySAML(SAMLCredential cred) throws UsernameNotFoundException {
        return cred.getAttributeAsString("surname");
    }
}
