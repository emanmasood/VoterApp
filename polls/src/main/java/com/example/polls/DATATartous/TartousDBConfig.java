package com.example.polls.DATATartous;



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
        entityManagerFactoryRef = "tartousEntityManagerFactory",
        transactionManagerRef = "tartousTransactionManager",
        basePackages = { "com.example.polls.DATATartous" }
)
public class TartousDBConfig {

    @Bean(name="tartousDataSource")
    @ConfigurationProperties(prefix="spring.tartousdatasource")
    public DataSource tartousDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "tartousEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tartousEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("tartousDataSource") DataSource tartousDataSource) {
        return builder
                .dataSource(tartousDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "tartousTransactionManager")
    public PlatformTransactionManager tartousTransactionManager(
            @Qualifier("tartousEntityManagerFactory") EntityManagerFactory tartousEntityManagerFactory) {
        return new JpaTransactionManager(tartousEntityManagerFactory);
    }
}

