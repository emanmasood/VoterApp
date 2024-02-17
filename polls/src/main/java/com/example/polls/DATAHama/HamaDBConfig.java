package com.example.polls.DATAHama;
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
        entityManagerFactoryRef = "hamaEntityManagerFactory",
        transactionManagerRef = "hamaTransactionManager",
        basePackages = { "com.example.polls.DATAHama" }
)
public class HamaDBConfig {

    @Bean(name="hamaDataSource")
    @ConfigurationProperties(prefix="spring.hamadatasource")
    public DataSource hamaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "hamaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean hamaEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("hamaDataSource") DataSource hamaDataSource) {
        return builder
                .dataSource(hamaDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "hamaTransactionManager")
    public PlatformTransactionManager hamaTransactionManager(
            @Qualifier("hamaEntityManagerFactory") EntityManagerFactory hamaEntityManagerFactory) {
        return new JpaTransactionManager(hamaEntityManagerFactory);
    }
}
