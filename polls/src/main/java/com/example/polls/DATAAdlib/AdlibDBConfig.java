package com.example.polls.DATAAdlib;

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
        entityManagerFactoryRef = "twoEntityManagerFactory",
        transactionManagerRef = "twoTransactionManager",
        basePackages = { "com.example.polls.DATAAdlib" }
)
public class AdlibDBConfig {

    @Bean(name="twoDataSource")
    @ConfigurationProperties(prefix="spring.twodatasource")
    public DataSource twoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "twoEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean twoEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("twoDataSource") DataSource twoDataSource) {
        return builder
                .dataSource(twoDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "twoTransactionManager")
    public PlatformTransactionManager twoTransactionManager(
            @Qualifier("twoEntityManagerFactory") EntityManagerFactory twoEntityManagerFactory) {
        return new JpaTransactionManager(twoEntityManagerFactory);
    }
}
