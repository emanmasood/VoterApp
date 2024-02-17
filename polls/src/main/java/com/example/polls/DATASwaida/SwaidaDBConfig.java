package com.example.polls.DATASwaida;



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
        entityManagerFactoryRef = "swaidaEntityManagerFactory",
        transactionManagerRef = "swaidaTransactionManager",
        basePackages = { "com.example.polls.DATASwaida" }
)
public class SwaidaDBConfig {

    @Bean(name="swaidaDataSource")
    @ConfigurationProperties(prefix="spring.swaidadatasource")
    public DataSource swaidaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "swaidaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean swaidaEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("swaidaDataSource") DataSource swaidaDataSource) {
        return builder
                .dataSource(swaidaDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "swaidaTransactionManager")
    public PlatformTransactionManager swaidaTransactionManager(
            @Qualifier("swaidaEntityManagerFactory") EntityManagerFactory swaidaEntityManagerFactory) {
        return new JpaTransactionManager(swaidaEntityManagerFactory);
    }
}

