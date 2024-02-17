package com.example.polls.DATALatakia;



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
        entityManagerFactoryRef = "latakiaEntityManagerFactory",
        transactionManagerRef = "latakiaTransactionManager",
        basePackages = { "com.example.polls.DATALatakia" }
)
public class LatakiaDBConfig {

    @Bean(name="latakiaDataSource")
    @ConfigurationProperties(prefix="spring.latakiadatasource")
    public DataSource latakiaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "latakiaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean latakiaEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("latakiaDataSource") DataSource latakiaDataSource) {
        return builder
                .dataSource(latakiaDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "latakiaTransactionManager")
    public PlatformTransactionManager latakiaTransactionManager(
            @Qualifier("latakiaEntityManagerFactory") EntityManagerFactory latakiaEntityManagerFactory) {
        return new JpaTransactionManager(latakiaEntityManagerFactory);
    }
}

