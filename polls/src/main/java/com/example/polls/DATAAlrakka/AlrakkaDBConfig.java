package com.example.polls.DATAAlrakka;

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
        entityManagerFactoryRef = "threeEntityManagerFactory",
        transactionManagerRef = "threeTransactionManager",
        basePackages = { "com.example.polls.DATAAlrakka" }
)
public class AlrakkaDBConfig {

    @Bean(name="threeDataSource")
    @ConfigurationProperties(prefix="spring.threedatasource")
    public DataSource threeDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "threeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean threeEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("threeDataSource") DataSource threeDataSource) {
        return builder
                .dataSource(threeDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "threeTransactionManager")
    public PlatformTransactionManager threeTransactionManager(
            @Qualifier("threeEntityManagerFactory") EntityManagerFactory threeEntityManagerFactory) {
        return new JpaTransactionManager(threeEntityManagerFactory);
    }
}
