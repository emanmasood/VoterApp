package com.example.polls.DATAHasakah;

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
        entityManagerFactoryRef = "hasakahEntityManagerFactory",
        transactionManagerRef = "hasakahTransactionManager",
        basePackages = { "com.example.polls.DATAHasakah" }
)
public class HasakahDBConfig {

    @Bean(name = "hasakahDataSource")
    @ConfigurationProperties(prefix = "spring.hasakahdatasource")
    public DataSource hasakahDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "hasakahEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean hasakahEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("hasakahDataSource") DataSource hasakahDataSource) {
        return builder
                .dataSource(hasakahDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "hasakahTransactionManager")
    public PlatformTransactionManager hasakahTransactionManager(
            @Qualifier("hasakahEntityManagerFactory") EntityManagerFactory hasakahEntityManagerFactory) {
        return new JpaTransactionManager(hasakahEntityManagerFactory);
    }
}