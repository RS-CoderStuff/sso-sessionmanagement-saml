package com.sessionmanagement.saml.controller;


import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.signature.impl.SignatureImpl;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Element;
import org.wso2.carbon.identity.sso.agent.SSOAgentException;
import org.wso2.carbon.identity.sso.agent.saml.X509CredentialImpl;
import org.wso2.carbon.identity.sso.agent.util.SSOAgentUtils;

import com.sessionmanagement.saml.bean.User;
import com.sessionmanagement.saml.config.SamlPropertyConfig;
import com.sessionmanagement.saml.sso.SSOConfigurationReader;
import com.sessionmanagement.saml.sso.SSOLoginService;
import com.sessionmanagement.saml.utils.Constants;

@Controller
@RequestMapping(value = "/sso/v1.0")
public class SSOSamlController {

	@Autowired
	public SamlPropertyConfig config;
	
	@Autowired
	public SSOLoginService loginService;

	
	
	@RequestMapping(value = "/saml/login", method = RequestMethod.GET)
	public  String   SamlSSOLogin() throws Exception {
		String redirectUrl = null;
		redirectUrl = loginService.getAuthNRedirectUrl(config.getIdpAppURL(), config.getRelayState(),
												config.getAssertionConsumerServiceUrl(), config.getIssuerId());
		return "redirect:" + redirectUrl;

	}
	
	@RequestMapping(value = "/saml/home", produces={"application/json"},
			method = RequestMethod.POST)
	@ResponseBody
	public  void   SamlSSOLanding(@RequestParam(name = "SAMLResponse") String samlResponse,
			@RequestParam(Constants.RELAY_STATE_PARAM) String relayState,
			HttpServletRequest request, HttpServletResponse res)
			throws Exception
	{
		System.out.println("samlResponse :: "+samlResponse);
		XMLObject response;
        org.opensaml.saml2.core.Response saml2Response = null;
        try {
            response = SSOAgentUtils.unmarshall(new String(Base64.decode(samlResponse), Charset.forName("UTF-8")));
            saml2Response = (org.opensaml.saml2.core.Response) response;
        } catch (SSOAgentException e) {
            String msg = "Error occurred while unmarshalling SAMLResponse.";
            System.err.println(msg+" :: "+ e);
        }

        try {
            validateSignature(saml2Response.getSignature());
            List<Assertion> assertions = saml2Response.getAssertions();
            Assertion assertion;
            if (assertions != null && assertions.size() > 0) {
                assertion = assertions.get(0);
                String subject = null;
                if (assertion.getSubject() != null && assertion.getSubject().getNameID() != null) {
                    subject = assertion.getSubject().getNameID().getValue();
                }
                if (subject != null) {
                   System.out.println("Subject in SAML :: "+subject);
                }
                User user = getUserInfoFromResponse(assertion);
                System.out.println(user.getFirstName());
                System.out.println(user.getLastName());
                System.out.println(user.getEmailAddress());

                
            }
            
        } catch (SSOAgentException e) {
            String msg = "Signature validation failed. Observed an attempt with invalid SAMLResponse.";
            System.err.println(msg+" :: "+ e);
        }
	}
	
	@RequestMapping(value = "/saml/test", produces={"application/json"},
			method = RequestMethod.GET)
	@ResponseBody
	public  void   TestSSOLogin() throws Exception
	{
		System.out.println("Test Appilication API");
	}
	
	
	private User getUserInfoFromResponse(Assertion assertion) {
		List<AttributeStatement> attributeStatement = assertion.getAttributeStatements();
		User user = new User();
		for(AttributeStatement attributeStatementName : attributeStatement) {
			List<Attribute> attributes = attributeStatementName.getAttributes();
			for(Attribute attributeName : attributes) {
				if(config.getEmailAddress().equals(attributeName.getName())) {
			    	  Element attributeValueElement=attributeName.getAttributeValues().get(0).getDOM();
			    	  user.setEmailAddress(attributeValueElement.getTextContent()); 
			      } else if(config.getFirstName().equals(attributeName.getName())) {
			    	  Element attributeValueElement=attributeName.getAttributeValues().get(0).getDOM();
			    	  user.setFirstName(attributeValueElement.getTextContent()); 
			      } else if(config.getLastName().equals(attributeName.getName())) {
			    	  Element attributeValueElement=attributeName.getAttributeValues().get(0).getDOM();
			    	  user.setLastName(attributeValueElement.getTextContent());
			      }
			}
		}
		return user;
	}
	 private void validateSignature(XMLObject signature) throws Exception {

	        SignatureImpl signImpl = (SignatureImpl) signature;
	        try {
	            SAMLSignatureProfileValidator signatureProfileValidator = new SAMLSignatureProfileValidator();
	            signatureProfileValidator.validate(signImpl);
	        } catch (ValidationException e) {
	            throw new Exception("Signature do not conform to SAML signature profile. Possible XML Signature " +
	                    "Wrapping Attack!", e);
	        }

	        SSOConfigurationReader ssoConfigurationReader = new SSOConfigurationReader();
	        try {
	            SignatureValidator validator = new SignatureValidator(
	                    new X509CredentialImpl(ssoConfigurationReader.getIdPX509Credential()));
	            validator.validate(signImpl);
	        } catch (ValidationException e) {
	            throw new Exception("Signature validation failed for the incoming SAML2 response.");
	        }
	    }
}
