package com.sessionmanagement.saml.sso;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.springframework.stereotype.Component;

@Component
public class SSOLoginService {

	  {
	    /* Initializing the OpenSAML library, Should be in some central place */
	    try {
	      DefaultBootstrap.bootstrap();
	    }
	    catch(Exception ex){
	      throw new RuntimeException("Unable to initialize SAML");
	    }
	  }
	  /*
	  * Return's redirectUrl
	  *  - Creates SAML2 AuthN object
	  *  - Compresses it
	  *  - Base 64 encode it
	  *  - URL encode it
	  *  - Appends RelayState
	  */
	  public String getAuthNRedirectUrl(String idpAppURL, String relayState, String assertionConsumerServiceUrl, String issuerId){

	    String url = null;

	    try {

	      AuthNRequestBuilder authNRequestBuilder = new AuthNRequestBuilder();
	      AuthnRequest authRequest = authNRequestBuilder.buildAuthenticationRequest(assertionConsumerServiceUrl, issuerId);
	      String samlRequest = generateSAMLRequest(authRequest);

	      // Prepare final Url
	      url = idpAppURL + "?SAMLRequest=" + samlRequest + "&RelayState=" + URLEncoder.encode(relayState,"UTF-8");

	    } catch (Exception ex) {
	      throw new RuntimeException("Unable to generate redirect Url");
	    }

	    return url;

	  }


	  /*
	   * Converts AuthN object to xml, compresses it, base64 encode it and url encode it
	   */
	  private String generateSAMLRequest(AuthnRequest authRequest) throws Exception {

	    Marshaller marshaller = org.opensaml.Configuration.getMarshallerFactory().getMarshaller(authRequest);
	    org.w3c.dom.Element authDOM = marshaller.marshall(authRequest);
	    StringWriter rspWrt = new StringWriter();
	    XMLHelper.writeNode(authDOM, rspWrt);
	    String messageXML = rspWrt.toString();

	    Deflater deflater = new Deflater(Deflater.DEFLATED, true);
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);
	    deflaterOutputStream.write(messageXML.getBytes());
	    deflaterOutputStream.close();
	    String samlRequest = Base64.encodeBytes(byteArrayOutputStream.toByteArray(), Base64.DONT_BREAK_LINES);
	    System.out.println("Saml Request :: "+samlRequest);
	    return URLEncoder.encode(samlRequest,"UTF-8");

	  }

}
