package com.example.firstspringbatch.controller;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BatchTriggerController {
    JobLauncher jobLauncher;
    Job job;
    @GetMapping("/jobLauncher/{id}")
    public void launchJob(@PathVariable String id) throws Exception{
        JobParameters jobParameters = new JobParametersBuilder().addString("param", id).toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
