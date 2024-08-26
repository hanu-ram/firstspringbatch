package com.example.firstspringbatch.config;

import com.example.firstspringbatch.decider.JobExecutionDeciderImpl;
import com.example.firstspringbatch.listner.BatchStepListener;
import com.example.firstspringbatch.listner.JobStatusListener;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
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
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class BatchConfigCustomFlow {
    JobRepository jobRepository;
    PlatformTransactionManager platformTransactionManager;
    BatchStepListener stepListener;
    JobExecutionDeciderImpl jobExecutionDecider;
    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet( ((contribution, chunkContext) -> {
                            System.out.println("Step1 Execution Completed");
                            return RepeatStatus.FINISHED;
                        })
                        , platformTransactionManager)
                .build();
    }
    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet( ((contribution, chunkContext) -> {
                            System.out.println("Step2 Execution Completed");
                            return RepeatStatus.FINISHED;
                        })
                        , platformTransactionManager)
//                .listener(stepListener)
                .build();
    }
    @Bean
    public Step step3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet( ((contribution, chunkContext) -> {
                            System.out.println("Step3 Execution Completed");
                            return RepeatStatus.FINISHED;
                        })
                        , platformTransactionManager)
                .build();
    }
    /**
     *  using listener to send custom exit status for step
     */
    /*@Bean
    public Job myJob() {
        return new JobBuilder("myJob", jobRepository)
                .start(step1())
                .on("COMPLETED")
                .to(step2())
                .from(step2())
                .on("EXECUTE_NEXT")
                .to(step3())
                .end()
                .build();
    }*/

    /**
     * using JobExecutionDeciderImpl for condition flow execution
     * @return Job
     */
    @Bean
    public Job myJob() {
        return new JobBuilder("myJob", jobRepository)
                .start(step1())
                .on("COMPLETED")
                .to(jobExecutionDecider)
                .on("UN-STORED")
                .to(step2())
                .from(jobExecutionDecider)
                .on("*")
                .to(step3())
                .end()
                .build();
    }

}
