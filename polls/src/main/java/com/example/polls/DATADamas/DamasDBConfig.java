package com.example.polls.DATADamas;


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
        entityManagerFactoryRef = "oneEntityManagerFactory",
        transactionManagerRef = "oneTransactionManager",
        basePackages = { "com.example.polls.DATADamas" }
)
public class DamasDBConfig {

    @Bean(name="oneDataSource")
    @ConfigurationProperties(prefix="spring.onedatasource")
    public DataSource oneDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oneEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oneEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("oneDataSource") DataSource oneDataSource) {
        return builder
                .dataSource(oneDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "oneTransactionManager")
    public PlatformTransactionManager oneTransactionManager(
            @Qualifier("oneEntityManagerFactory") EntityManagerFactory oneEntityManagerFactory) {
        return new JpaTransactionManager(oneEntityManagerFactory);
    }
}
