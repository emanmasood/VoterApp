package com.example.polls.DATAQuneitra;



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
        entityManagerFactoryRef = "quneitraEntityManagerFactory",
        transactionManagerRef = "quneitraTransactionManager",
        basePackages = { "com.example.polls.DATAQuneitra" }
)
public class QuneitraDBConfig {

    @Bean(name="quneitraDataSource")
    @ConfigurationProperties(prefix="spring.quneitradatasource")
    public DataSource quneitraDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "quneitraEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean quneitraEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("quneitraDataSource") DataSource quneitraDataSource) {
        return builder
                .dataSource(quneitraDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "quneitraTransactionManager")
    public PlatformTransactionManager quneitraTransactionManager(
            @Qualifier("quneitraEntityManagerFactory") EntityManagerFactory quneitraEntityManagerFactory) {
        return new JpaTransactionManager(quneitraEntityManagerFactory);
    }
}

