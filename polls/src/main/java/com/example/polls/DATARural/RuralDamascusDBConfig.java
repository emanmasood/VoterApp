package com.example.polls.DATARural;



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
        entityManagerFactoryRef = "ruraldamascusEntityManagerFactory",
        transactionManagerRef = "ruraldamascusTransactionManager",
        basePackages = { "com.example.polls.DATARural" }
)
public class RuralDamascusDBConfig {

    @Bean(name="ruraldamascusDataSource")
    @ConfigurationProperties(prefix="spring.ruraldamascusdatasource")
    public DataSource ruraldamascusDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ruraldamascusEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ruraldamascusEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("ruraldamascusDataSource") DataSource ruraldamascusDataSource) {
        return builder
                .dataSource(ruraldamascusDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "ruraldamascusTransactionManager")
    public PlatformTransactionManager ruraldamascusTransactionManager(
            @Qualifier("ruraldamascusEntityManagerFactory") EntityManagerFactory ruraldamascusEntityManagerFactory) {
        return new JpaTransactionManager(ruraldamascusEntityManagerFactory);
    }
}

