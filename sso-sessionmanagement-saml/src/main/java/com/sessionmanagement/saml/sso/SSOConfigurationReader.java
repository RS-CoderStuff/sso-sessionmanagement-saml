package com.sessionmanagement.saml.sso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.util.ResourceUtils;
import org.wso2.carbon.identity.sso.agent.SSOAgentException;
import org.wso2.carbon.identity.sso.agent.saml.SSOAgentX509Credential;
import org.wso2.carbon.identity.sso.agent.saml.SSOAgentX509KeyStoreCredential;

import com.sessionmanagement.saml.utils.Constants;

public class SSOConfigurationReader {


    /**
     * Read {@link Constants#JKS_FILE_NAME} JKS file and return X509Credential of Identity Provider.
     * @return X509Credential of Identity Server.
     * @throws TestGridException if an error occur while reading JKS file.
     */
    public SSOAgentX509Credential getIdPX509Credential() throws Exception {
        Properties properties = getSSOProperties();
        try {
			/*
			 * java.nio.file.Path configPath = Paths. get(Constants.CERT_PATH,
			 * Constants.SSO_DIRECTORY, Constants.JKS_FILE_NAME);
			 * System.out.println("Config Path :: "+configPath.getFileName()); InputStream
			 * keyStoreInputStream = Files.newInputStream(configPath);
			 */
        	File file = ResourceUtils.getFile("classpath:wso2carbon.jks");
        	InputStream keyStoreInputStream = new FileInputStream(file);
            SSOAgentX509Credential credential;

            credential = new SSOAgentX509KeyStoreCredential(keyStoreInputStream,
                    properties.getProperty(Constants.PROPERTYNAME_KEYSTORE_PASSWORD).toCharArray(),
                    properties.getProperty(Constants.PROPERTYNAME_IDP_PUBLIC_KEY_ALIAS),
                    properties.getProperty(Constants.PROPERTYNAME_PRIVATE_KEY_ALIAS),
                    properties.getProperty(Constants.PROPERTYNAME_PRIVATE_KEY_PASSWORD).toCharArray());
            return credential;
        } catch (IOException | SSOAgentException e) {
            throw new Exception("Error occurred while reading JKS file to fetch IdP's credential.", e);
        }
    }

    /**
     * Load properties from {@link Constants#SSO_PROPERTY_FILE_NAME}.
     * @return All properties in the property file.
     */
    Properties getSSOProperties() throws Exception {
        Properties properties = new Properties();
        try {
        	File file = ResourceUtils.getFile("classpath:samlProperty.properties");
        	InputStream propertyStream = new FileInputStream(file);
        	properties.load(propertyStream);
        } catch (IOException e) {
            throw new Exception("Error occurred while reading SSO property file.", e);
        }
        return properties;
    }
    
}
