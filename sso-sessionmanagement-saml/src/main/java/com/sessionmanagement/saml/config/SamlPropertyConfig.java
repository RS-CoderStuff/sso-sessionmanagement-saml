package com.sessionmanagement.saml.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:samlProperty.properties")
public class SamlPropertyConfig {

	@Value("${KeyStorePassword}")
    private String keyStorePassword;

	@Value("${IdPPublicCertAlias}")
    private String idPPublicCertAlias;
	
	@Value("${PrivateKeyAlias}")
    private String privateKeyAlias;
	
	@Value("${PrivateKeyPassword}")
    private String privateKeyPassword;
	
	@Value("${firstName}")
    private String firstName;
	
	@Value("${lastName}")
    private String lastName;
	
	@Value("${emailAddress}")
    private String emailAddress;
	
	@Value("${idpAppURL}")
    private String idpAppURL;
	
	@Value("${relayState}")
    private String relayState;
	
	@Value("${assertionConsumerServiceUrl}")
    private String assertionConsumerServiceUrl;
	
	@Value("${issuerId}")
    private String issuerId;

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public String getIdPPublicCertAlias() {
		return idPPublicCertAlias;
	}

	public void setIdPPublicCertAlias(String idPPublicCertAlias) {
		this.idPPublicCertAlias = idPPublicCertAlias;
	}

	public String getPrivateKeyAlias() {
		return privateKeyAlias;
	}

	public void setPrivateKeyAlias(String privateKeyAlias) {
		this.privateKeyAlias = privateKeyAlias;
	}

	public String getPrivateKeyPassword() {
		return privateKeyPassword;
	}

	public void setPrivateKeyPassword(String privateKeyPassword) {
		this.privateKeyPassword = privateKeyPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getIdpAppURL() {
		return idpAppURL;
	}

	public void setIdpAppURL(String idpAppURL) {
		this.idpAppURL = idpAppURL;
	}

	public String getRelayState() {
		return relayState;
	}

	public void setRelayState(String relayState) {
		this.relayState = relayState;
	}

	public String getAssertionConsumerServiceUrl() {
		return assertionConsumerServiceUrl;
	}

	public void setAssertionConsumerServiceUrl(String assertionConsumerServiceUrl) {
		this.assertionConsumerServiceUrl = assertionConsumerServiceUrl;
	}

	public String getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}
}
