package com.sessionmanagement.saml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@ServletComponentScan
@SpringBootApplication
public class SessionManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SessionManagementApplication.class, args);
	}

}
