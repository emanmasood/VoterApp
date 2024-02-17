package com.example.polls.DATAHalab;
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
        entityManagerFactoryRef = "halabEntityManagerFactory",
        transactionManagerRef = "halabTransactionManager",
        basePackages = { "com.example.polls.DATAHalab" }
)
public class HalabDBConfig {
    @Bean(name="halabDataSource")
    @ConfigurationProperties(prefix="spring.halabdatasource")
    public DataSource halabDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "halabEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean halabEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("halabDataSource") DataSource halabDataSource) {
        return builder
                .dataSource(halabDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "halabTransactionManager")
    public PlatformTransactionManager halabTransactionManager(
            @Qualifier("halabEntityManagerFactory") EntityManagerFactory halabEntityManagerFactory) {
        return new JpaTransactionManager(halabEntityManagerFactory);
    }
}
