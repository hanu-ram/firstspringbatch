package com.example.firstspringbatch.controller;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BatchTriggerController {
    JobLauncher jobLauncher;

    Job job;

    @GetMapping("/jobLauncher")
    public void handle() throws Exception{
        jobLauncher.run(job, new JobParameters());
    }
}
