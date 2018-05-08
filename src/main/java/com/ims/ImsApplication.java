package com.ims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;

@SpringBootApplication
// @EnableScheduling
public class ImsApplication {
	
	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(new Object[] { ImsApplication.class }, args);
	}

	@Bean
	public DefaultFtpSessionFactory ftpSessionFactory() {
		DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
		factory.setHost((String)env.getProperty("ftp.location"));
		factory.setUsername((String)env.getProperty("ftp.username"));
		factory.setPassword((String)env.getProperty("ftp.password"));
		return factory;
	}

	@Bean
	public FtpRemoteFileTemplate template(DefaultFtpSessionFactory factory) {
		return new FtpRemoteFileTemplate(factory);
	}
	

}