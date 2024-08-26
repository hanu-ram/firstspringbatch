package com.example.firstspringbatch.listner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobStatusListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("BeforeJob - Job Name: " + jobExecution.getJobInstance().getJobName());
        System.out.println("BeforeJob - Job Parameters:  " + jobExecution.getJobParameters());
        System.out.println("BeforeJob - Job ExecutionContext:  " + jobExecution.getExecutionContext());
        System.out.println(jobExecution.getExecutionContext().isDirty());
        jobExecution.getExecutionContext().put("jobParam1", "jobValue1");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("AfterJob - Job Name: " + jobExecution.getJobInstance().getJobName());
        System.out.println("AfterJob - Job Parameters:  " + jobExecution.getJobParameters());
        System.out.println("AfterJob - Job ExecutionContext:  " + jobExecution.getExecutionContext());
        System.out.println(jobExecution.getExecutionContext().isDirty());
    }
}
