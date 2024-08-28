package com.example.firstspringbatch.tasklet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@ConditionalOnProperty(value = "custom.flow.enabled", havingValue = "true")
public class TaskletBatchTriggerController {
    private final JobLauncher jobLauncher;
    private final Job job;
    public TaskletBatchTriggerController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }
    @GetMapping("/taskletJobLauncher/{id}")
    public void launchJob(@PathVariable String id) {
        JobParameters jobParameters = new JobParametersBuilder().addString("chunkParam", id).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error("ChunkBatchTriggerController::launchJob Exception occured {}", e.getMessage());
        }
    }
}
