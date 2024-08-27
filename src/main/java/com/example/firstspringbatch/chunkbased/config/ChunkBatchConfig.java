package com.example.firstspringbatch.chunkbased.config;

import com.example.firstspringbatch.chunkbased.reader.ProductItemReader;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Configuration
@AllArgsConstructor
public class ChunkBatchConfig {
    JobRepository jobRepository;
    PlatformTransactionManager platformTransactionManager;

    @Bean
    public ProductItemReader productItemReader() {
        List<String> productList = new ArrayList<>(11);
        IntStream.range(0, 10).forEach(i -> productList.add("Product " + i));
        return new ProductItemReader(productList);
    }
    @Bean
    public Step chunkStep1() {
        return new StepBuilder("chunkStep1", jobRepository)
                .<String, String>chunk(3, platformTransactionManager)
                .reader(productItemReader())
                .writer(item -> {
                    System.out.println("Chunk Writer Executed");
                    item.forEach(System.out::println);
                    System.out.println("Chunk Writer Ended");
                }).build();
    }
    @Bean
    public Job myChunkJob() {
        return new JobBuilder("myChunkJob", jobRepository)
                .start(chunkStep1())
                .build();
    }

}
