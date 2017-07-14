package com.example.demo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.demo.batch.mapper.DemoFieldSetMapper;
import com.example.demo.batch.processor.DemoItemProcessor;
import com.example.demo.batch.writer.DemoItemWriter;
import com.example.demo.domain.Employee;

@Configuration
@EnableBatchProcessing
public class BatchJob {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job demoJob(){
	return jobs.get("demoJob")
	.start(step())
	.build();
	}
	
	@Bean
	public Step step(){
	return stepBuilderFactory.get("step")
	.<Employee,Employee>chunk(5) //important to be one in this case to commit after every line read
	.reader(reader())
	.processor(processor())
	.writer(writer())
	.faultTolerant()
//	.skipLimit(10) //default is set to 0
	.build();
	}
	
	@Bean
	public ItemReader<Employee> reader() {
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setLinesToSkip(1);// first line is title definition
		reader.setResource(new ClassPathResource("inputFile.txt"));
		reader.setLineMapper(lineMapper());
		return reader;
	}
	
	@Bean
	public LineMapper<Employee> lineMapper() {
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<Employee>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames(new String[] {"id", "name"});

		BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<Employee>();
		fieldSetMapper.setTargetType(Employee.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(demoFieldSetMapper());

		return lineMapper;
	}
	
	@Bean
	public DemoFieldSetMapper demoFieldSetMapper() {
	return new DemoFieldSetMapper();
	}




	/** configure the processor related stuff */
	@Bean
	public ItemProcessor<Employee, Employee> processor() {
		return new DemoItemProcessor();
	}




	@Bean
	public ItemWriter<Employee> writer() {
		return new DemoItemWriter();
	}
	// end::readerwriterprocessor[]





}
