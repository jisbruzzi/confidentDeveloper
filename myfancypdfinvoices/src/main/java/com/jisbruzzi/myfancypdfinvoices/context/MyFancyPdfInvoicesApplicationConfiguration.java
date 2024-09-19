package com.jisbruzzi.myfancypdfinvoices.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jisbruzzi.myfancypdfinvoices.ApplicationLauncher;
import com.jisbruzzi.myfancypdfinvoices.service.InvoiceService;
import com.jisbruzzi.myfancypdfinvoices.service.UserService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackageClasses = ApplicationLauncher.class)
@PropertySource("classpath:/application.properties")
@PropertySource(value="classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class MyFancyPdfInvoicesApplicationConfiguration {
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
