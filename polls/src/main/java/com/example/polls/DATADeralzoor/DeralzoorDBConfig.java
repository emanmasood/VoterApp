package com.example.polls.DATADeralzoor;



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
        entityManagerFactoryRef = "deralzoorEntityManagerFactory",
        transactionManagerRef = "deralzoorTransactionManager",
        basePackages = { "com.example.polls.DATADeralzoor" }
)
public class DeralzoorDBConfig {
    @Bean(name="deralzoorDataSource")
    @ConfigurationProperties(prefix="spring.deralzoordatasource")
    public DataSource deralzoorDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "deralzoorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean deralzoorEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("deralzoorDataSource") DataSource deralzoorDataSource) {
        return builder
                .dataSource(deralzoorDataSource)
                .packages("com.example.polls.model")
                .build();
    }

    @Bean(name = "deralzoorTransactionManager")
    public PlatformTransactionManager deralzoorTransactionManager(
            @Qualifier("deralzoorEntityManagerFactory") EntityManagerFactory deralzoorEntityManagerFactory) {
        return new JpaTransactionManager(deralzoorEntityManagerFactory);
    }
}
