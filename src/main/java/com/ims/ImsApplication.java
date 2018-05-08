package com.ims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;

@SpringBootApplication
// @EnableScheduling
public class ImsApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(new Object[] { ImsApplication.class }, args);
	}

	@Bean
	public DefaultFtpSessionFactory ftpSessionFactory() {
		DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
		sf.setHost("192.168.241.12");
		sf.setUsername("rbongurala");
		sf.setPassword("April@2018");
		return sf;
	}

	@Bean
	public FtpRemoteFileTemplate template(DefaultFtpSessionFactory sf) {
		return new FtpRemoteFileTemplate(sf);
	}
	

}