package org.springframework.security.saml.web;

import org.opensaml.common.SAMLException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.AttributeStatementImpl;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.xml.encryption.DecryptionException;
import org.opensaml.xml.schema.impl.XSAnyImpl;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;

public class NameIdWebSSOProfileConsumer extends WebSSOProfileConsumerImpl {

    @Override
    protected void verifySubject(Subject subject, AuthnRequest request, SAMLMessageContext context)
            throws SAMLException, DecryptionException {
        super.verifySubject(subject, request, context);

        NameID nameId = new NameIDBuilder().buildObject();
        Response response = (Response) context.getInboundSAMLMessage();
        for (EncryptedAssertion ea : response.getEncryptedAssertions()) {
            Assertion assertion = context.getLocalDecrypter().decrypt(ea);

            for (Statement statement : assertion.getStatements()) {
                if (statement instanceof AttributeStatementImpl) {
                    for (Attribute attribute : ((AttributeStatementImpl) statement).getAttributes()) {
                        if ("urn:oid:1.2.840.113549.1.9.1".equals(attribute.getName())) {

                            XSAnyImpl xmlObject = (XSAnyImpl) attribute.getAttributeValues().get(0);
                            nameId.setValue(xmlObject.getTextContent().toLowerCase());
                            // noinspection unchecked
                            context.setSubjectNameIdentifier(nameId);
                        }
                    }
                }
            }
        }
    }
}
