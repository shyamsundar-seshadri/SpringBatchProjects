package com.example.demo.controller;

import java.util.Date;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/controller")
@RestController
public class DemoController {

	@Autowired
	private ApplicationContext appContext;
	
	
	@RequestMapping( value = "/upload", method=RequestMethod.GET)
	public String controllerMethod() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, InterruptedException {
		
		JobLauncher jobLauncher = appContext.getBean(JobLauncher.class);
//		ClassPathResource sp = new ClassPathResource(null);
//		System.out.println(sp.getPath());
		Job job = appContext.getBean("demoJob", Job.class);
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addDate("date", new Date())
				.toJobParameters(); 
		//If you need to add any more parameters
		
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		

		BatchStatus batchStatus = jobExecution.getStatus();
		
		while(batchStatus.isRunning()){
			System.out.println("*********** Still running.... **************");
			Thread.sleep(1000);
			}
		jobExecution.stop();
		return "Success";
	}
}
