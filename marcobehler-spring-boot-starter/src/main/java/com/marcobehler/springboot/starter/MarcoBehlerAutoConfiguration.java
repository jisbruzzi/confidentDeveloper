package com.marcobehler.springboot.starter;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.dbcp.ViburDataSource;

import javax.sql.DataSource;

@Configuration  //
@ConditionalOnClass(ViburDataSource.class)
@ConditionalOnMissingBean(DataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MarcoBehlerDataSourceProperties.class)
public class MarcoBehlerAutoConfiguration {

	@Bean
	public ViburDBCPDataSource dataSource(MarcoBehlerDataSourceProperties properties) {
		ViburDBCPDataSource ds = new ViburDBCPDataSource();
		ds.setJdbcUrl(properties.getUrl());
		ds.setUsername(properties.getUsername());
		ds.setPassword(properties.getPassword());
		ds.setDriverClassName(properties.getDriverClassName());
		ds.start();
		return ds;
	}
}