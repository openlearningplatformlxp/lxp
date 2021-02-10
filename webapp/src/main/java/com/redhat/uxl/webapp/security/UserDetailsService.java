package com.redhat.uxl.webapp.security;

import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.datalayer.repository.IntroCourseRepository;
import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.IntroCourse;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.errors.PersonServiceErrorCode;
import com.redhat.uxl.services.service.PersonService;
import com.redhat.uxl.services.service.ProfileService;
import com.redhat.uxl.services.service.TotaraEnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type User details service.
 */
@Component("userDetailsService")
@Slf4j
public class UserDetailsService
        implements org.springframework.security.core.userdetails.UserDetailsService, SAMLUserDetailsService {

    @Inject
    private PersonRepository personRepository;

    @Inject
    private PersonService personService;

    @Inject
    private ProfileService profileService;

    @Inject
    private TotaraEnrollmentService totaraEnrollmentService;

    @Inject
    private IntroCourseRepository introCourseRepository;

    private String getAttributeValue(XMLObject attributeValue) {
        return attributeValue == null ? null
                : attributeValue instanceof XSString ? getStringAttributeValue((XSString) attributeValue)
                        : attributeValue instanceof XSAnyImpl ? getAnyAttributeValue((XSAnyImpl) attributeValue)
                                : attributeValue.toString();
    }

    private String getStringAttributeValue(XSString attributeValue) {
        return attributeValue.getValue();
    }

    private String getAnyAttributeValue(XSAnyImpl attributeValue) {
        return attributeValue.getTextContent();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        TotaraUserDTO tu = null;
        String lowercaseLogin = login.toLowerCase();
        Person personFromDatabase = null;


        try {
            // See if they are in this DB
            personFromDatabase = personService.getPersonByEmail(lowercaseLogin, true);
            if (personFromDatabase == null) {
                // could be impersonating
                personFromDatabase = personService.getPersonByLogin(lowercaseLogin, true);
            }

            if (personFromDatabase == null) {
                log.error("New Person!!");
                tu = profileService.getTotaraUserProfile(lowercaseLogin);
                personFromDatabase = createPerson(tu, lowercaseLogin);

                log.error("Checking to see if user should be in intro course..");
                IntroCourse ic = introCourseRepository.findIntroCourse();
                if (ic != null) {
                    if (ic.getIsProgram() == 1) {
                        log.error("Enrolling in program...");
                        totaraEnrollmentService.enrollUserToProgram(new Long(ic.getCourseId()), tu.getId());
                    } else {
                        log.error("Enrolling in course...");
                        totaraEnrollmentService.enrollUserToCourse(new Long(ic.getCourseId()), tu.getId());
                    }
                }

            }
            if (personFromDatabase == null) {
                throw new UsernameNotFoundException("Person " + lowercaseLogin + " was not found in the database");
            } else if (personFromDatabase.isDisabled()) {
                throw new GeneralException(PersonServiceErrorCode.PERSON_DISABLED);
            } else if (!personFromDatabase.isActivated()) {
                throw new PersonNotActivatedException("Person " + lowercaseLogin + " was not activated");
            } else if (StrUtils.isBlank(personFromDatabase.getPassword())) {
                throw new GeneralException(PersonServiceErrorCode.BAD_PASSWORD);
            }

            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Authority authority : personFromDatabase.getAuthorities()) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
                grantedAuthorities.add(grantedAuthority);
            }

            return new org.springframework.security.core.userdetails.User(personFromDatabase.getLogin(),
                    personFromDatabase.getPassword(), grantedAuthorities);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("User with email " + lowercaseLogin + " not found.");
            throw new UsernameNotFoundException("Your account has not yet been provisioned");
        }

    }

    @Override
    @Transactional
    public Object loadUserBySAML(SAMLCredential samlCredential) throws UsernameNotFoundException {
        log.debug("Authenticating SAMLCredential {}", samlCredential.toString());

        String email = null;
        String RHUUID = null;

        List<Attribute> attributeList = samlCredential.getAttributes();
        if (CollectionUtils.isEmpty(attributeList)) {
            throw new UsernameNotFoundException("No SAMLCredential Attributes returned from IDP");
        } else {
            for (Attribute attribute : attributeList) {
                log.debug("Checking attribute {} for uid in friendlyName", attribute);

                if ("urn:oid:1.2.840.113549.1.9.1".equalsIgnoreCase(attribute.getName())) {
                    XMLObject xmlObject = attribute.getAttributeValues().get(0);

                    email = getAttributeValue(xmlObject);

                }
                if ("rhuuid".equalsIgnoreCase(attribute.getName())) {
                    XMLObject xmlObject = attribute.getAttributeValues().get(0);

                    RHUUID = getAttributeValue(xmlObject);
                }
            }
            Person personFromDatabase = null;

            TotaraUserDTO tu = null;
            String lowercaseLogin = email.toLowerCase();

            try {
                // See if they are in this DB
                personFromDatabase = personService.getPersonByEmail(lowercaseLogin, true);

                if (personFromDatabase == null) {
                    log.error("New Person!!");
                    tu = profileService.getTotaraUserProfile(lowercaseLogin);
                    personFromDatabase = createPerson(tu, lowercaseLogin);

                    log.error("Checking to see if user should be in intro course..");
                    IntroCourse ic = introCourseRepository.findIntroCourse();
                    if (ic != null) {
                        if (ic.getIsProgram() == 1) {
                            log.error("Enrolling in program...");
                            totaraEnrollmentService.enrollUserToProgram(new Long(ic.getCourseId()), tu.getId());
                        } else {
                            log.error("Enrolling in course...");
                            totaraEnrollmentService.enrollUserToCourse(new Long(ic.getCourseId()), tu.getId());
                        }
                    }

                }
                if (personFromDatabase == null) {
                    throw new UsernameNotFoundException("Person " + lowercaseLogin + " was not found in the database");
                } else if (personFromDatabase.isDisabled()) {
                    throw new GeneralException(PersonServiceErrorCode.PERSON_DISABLED);
                } else if (!personFromDatabase.isActivated()) {
                    throw new PersonNotActivatedException("Person " + lowercaseLogin + " was not activated");
                } else if (StrUtils.isBlank(personFromDatabase.getPassword())) {
                    throw new GeneralException(PersonServiceErrorCode.BAD_PASSWORD);
                }

                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                for (Authority authority : personFromDatabase.getAuthorities()) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
                    grantedAuthorities.add(grantedAuthority);
                }

                return new org.springframework.security.core.userdetails.User(personFromDatabase.getLogin(),
                        personFromDatabase.getPassword(), grantedAuthorities);

            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                log.error("User with email " + lowercaseLogin + " not found.");
                throw new UsernameNotFoundException("Your account has not yet been provisioned");
            }
        }

    }

    private Person createPerson(TotaraUserDTO tu, String lowercaseLogin) {
        Person personFromDatabase = new Person();

        personFromDatabase.setEmail(lowercaseLogin);
        personFromDatabase.setFirstName(tu.getFirstName());
        personFromDatabase.setLastName(tu.getLastName());
        personFromDatabase.setLogin(tu.getId() + "");
        personFromDatabase.setPassword("12345324");
        personFromDatabase.setActivated(true);
        personFromDatabase.setLangKey("en");

        Set<String> authority = new HashSet<String>();
        authority.add("ROLE_USER");

        return personService.createPerson(personFromDatabase, authority);

    }
}
