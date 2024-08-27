package com.example.firstspringbatch.chunkbased.controller;

import com.example.firstspringbatch.chunkbased.reader.ProductItemReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@Slf4j
public class ChunkBatchTriggerController {
    private final JobLauncher jobLauncher;
    private final Job job;
    public ChunkBatchTriggerController(JobLauncher jobLauncher, @Qualifier("myChunkJob") Job job) {
        System.out.println(job.getName());
        this.jobLauncher = jobLauncher;
        this.job = job;
    }
    @GetMapping("/chunkJobLauncher/{id}")
    public void launchJob(@PathVariable String id) {
        JobParameters jobParameters = new JobParametersBuilder().addString("chunkParam", id).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error("ChunkBatchTriggerController::launchJob Exception occured {}", e.getMessage());
        }
    }
}
