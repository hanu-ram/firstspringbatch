package com.example.firstspringbatch.itemreaderprac.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ItemBatchTriggerController {
    private final JobLauncher jobLauncher;
    private final Job job;
    @Value("${custom.my:Default}")
    public String s;
    public ItemBatchTriggerController(JobLauncher jobLauncher, @Qualifier("itemProcessJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }
    @GetMapping("/itemJobLauncher/{id}")
    public void launchJob(@PathVariable String id) {
        System.out.println("s = " + s);
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("itemBatchProcessParam", id)
                    .toJobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error("ChunkBatchTriggerController::launchJob Exception occured {}", e.getMessage());
        }
    }
}
