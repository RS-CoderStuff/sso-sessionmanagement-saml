package com.sessionmanagement.saml.utils;

public class Constants {

	public static final String WEBAPP_CONTEXT = "/";
	
	/* Constants relates to SSO configurations. */
	
    public static final String LOGIN_URI = WEBAPP_CONTEXT + "login";
    public static final String JKS_FILE_NAME = "\\wso2carbon.jks";
    public static final String SSO_DIRECTORY = "\\SSO";
    public static final String CERT_PATH = "F:\\Cert";
    public static final String RELAY_STATE_PARAM = "RelayState";
    public static final String QUERY_PARAM_APPEND = "&";
    public static final String QUERY_PARAM_EQUAL = "=";
    public static final int SESSION_TIME = 1800;
    
    public static final String PROPERTYNAME_KEYSTORE_PASSWORD = "KeyStorePassword";
    public static final String PROPERTYNAME_PRIVATE_KEY_ALIAS = "PrivateKeyAlias";
    public static final String PROPERTYNAME_PRIVATE_KEY_PASSWORD = "PrivateKeyPassword";
    public static final String PROPERTYNAME_IDP_PUBLIC_KEY_ALIAS = "IdPPublicCertAlias";
}
