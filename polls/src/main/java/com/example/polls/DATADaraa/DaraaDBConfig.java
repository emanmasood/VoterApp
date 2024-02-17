package com.example.polls.DATADaraa;
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
        entityManagerFactoryRef = "daraaEntityManagerFactory",
        transactionManagerRef = "daraaTransactionManager",
        basePackages = { "com.example.polls.DATADaraa" }
)
public class DaraaDBConfig {


    @Bean(name="daraaDataSource")
    @ConfigurationProperties(prefix="spring.daraadatasource")
    public DataSource daraaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "daraaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean daraaEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("daraaDataSource") DataSource daraaDataSource) {
        return builder
                .dataSource(daraaDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "daraaTransactionManager")
    public PlatformTransactionManager daraaTransactionManager(
            @Qualifier("oneEntityManagerFactory") EntityManagerFactory daraaEntityManagerFactory) {
        return new JpaTransactionManager(daraaEntityManagerFactory);
    }
}
