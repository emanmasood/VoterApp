package com.example.polls.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "EntityManagerFactory",
        transactionManagerRef = "defaultTransactionManager",
        basePackages = { "com.example.polls.repository" }
)

//@ComponentScan(basePackages = { "com.example.polls.controller" })

public class DBconfig {


    @Bean(name="DataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "EntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean EntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                              @Qualifier("DataSource") DataSource DataSource) {
        return builder
                .dataSource(DataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "defaultTransactionManager")
    @Primary
    public PlatformTransactionManager defaultTransactionManager(
            @Qualifier("EntityManagerFactory") EntityManagerFactory EntityManagerFactory) {
        return new JpaTransactionManager(EntityManagerFactory);
    }


}
