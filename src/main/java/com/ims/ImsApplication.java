package com.ims;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SpringBootApplication
@EnableScheduling
public class ImsApplication implements CommandLineRunner {
	
	private static final Logger LOG = Logger.getLogger(ImsApplication.class);
	
	@Autowired
	private Environment env;
	
	@Value("${spring.profiles.active}")
	private String applicationName;
	
	public static void main(String[] args) {
		SpringApplication.run(new Object[] { ImsApplication.class }, args);
	}

	@Bean
	public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
		SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		
		factoryBean.setJobFactory(jobFactory);
		factoryBean.setApplicationContextSchedulerContextKey("applicationContext");
		return factoryBean;
	}

	@Override
	public void run(String... arg0) throws Exception {
		LOG.info("Application running on:"+applicationName);
	}
	

}