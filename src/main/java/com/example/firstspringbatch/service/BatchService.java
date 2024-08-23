package com.example.firstspringbatch.service;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.support.DatabaseType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager", tablePrefix = "MY_")
public class BatchService {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.custom")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager batchTransactionManager(DataSource dataSource) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setDataSource(dataSource);
        return jpaTransactionManager;
    }

    /* @Bean(name = "batchTransactionManager")
     @Primary
     public PlatformTransactionManager batchTransactionManager(LocalContainerEntityManagerFactoryBean studentEntityManagerFactory) {
         return new JpaTransactionManager(Objects.requireNonNull(studentEntityManagerFactory.getObject()));
     }*/
    @Bean
    @Primary
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTransactionManager(transactionManager);
        factoryBean.setDatabaseType(DatabaseType.MYSQL.getProductName());
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @Bean
    public Step taskletStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("taskletStep2", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("Executed the tasklet step");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();

    }

    @Bean
    public Job footballJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("job1", jobRepository)
                .start(taskletStep(jobRepository, platformTransactionManager))
                .build();
    }


}
