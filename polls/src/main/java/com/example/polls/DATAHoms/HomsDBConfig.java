package com.example.polls.DATAHoms;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "homsEntityManagerFactory",
        transactionManagerRef = "homsTransactionManager",
        basePackages = { "com.example.polls.DATAHoms" }
)
public class HomsDBConfig {

    @Bean(name="homsDataSource")
    @ConfigurationProperties(prefix="spring.homsdatasource")
    public DataSource homsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "homsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean homsEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("homsDataSource") DataSource homsDataSource) {
        return builder
                .dataSource(homsDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "homsTransactionManager")
    public PlatformTransactionManager homsTransactionManager(
            @Qualifier("homsEntityManagerFactory") EntityManagerFactory homsEntityManagerFactory) {
        return new JpaTransactionManager(homsEntityManagerFactory);
    }
}

