package com.example.firstspringbatch.listner;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class BatchStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("BeforeStep - Step Name: " + stepExecution.getStepName());
        System.out.println("BeforeStep - Step Parameters:  " + stepExecution.getJobExecution().getJobInstance());
        System.out.println("BeforeStep - Step ExecutionContext:  " + stepExecution.getExecutionContext());
        System.out.println("BeforeStep - Job ExecutionContext:  " + stepExecution.getJobExecution().getExecutionContext());
        System.out.println(stepExecution.getExecutionContext().isDirty());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("AfterStep - Step Name: " + stepExecution.getStepName());
        System.out.println("AfterStep - Step Parameters:  " + stepExecution.getJobExecution().getJobInstance());
        System.out.println("AfterStep - Step ExecutionContext:  " + stepExecution.getExecutionContext());
        System.out.println(stepExecution.getExecutionContext().isDirty());
        return new ExitStatus("EXECUTE_NEXT");
    }
}
