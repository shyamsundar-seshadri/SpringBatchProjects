package com.example.demo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.demo.batch.mapper.DemoFieldSetMapper;
import com.example.demo.batch.processor.DemoItemProcessor;
import com.example.demo.batch.writer.ExcelFileItemWriter;
import com.example.demo.domain.Employee;

@Configuration
@EnableBatchProcessing
public class CSVBatchJob {

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
	
	private int rownum = 1;

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
	@StepScope
	public FlatFileItemReader<Employee> reader() {
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
	@StepScope
	@Bean
	public ItemProcessor<Employee, Employee> processor() {
		return new DemoItemProcessor();
	}


	@StepScope
	@Bean
	public ItemWriter<Employee> writer() {
		
		ExcelFileItemWriter writer =  new ExcelFileItemWriter();
		writer.setRownum(rownum);
		writer.setOutputFileLocation("output.xlsx");
		/*FlatFileItemWriter<Employee> writer = new FlatFileItemWriter<Employee>();
		writer.setResource(new ClassPathResource("C://outputFile.txt"));
//		writer.setName("Something");
		writer.setLineAggregator(lineAggregator());
//		writer.setAppendAllowed(true);
		writer.setShouldDeleteIfExists(true);
		writer.setLineSeparator(",");
		writer.setHeaderCallback(new FileHeaderCallBack()); */
		return writer;
	}
	
	


	@Bean
	public LineAggregator lineAggregator() {
		DelimitedLineAggregator aggr = new DelimitedLineAggregator<Employee>();
		aggr.setDelimiter("|");
		aggr.setFieldExtractor(fieldExtractor());
		return aggr;
	}
	// end::readerwriterprocessor[]
	
	@Bean
	public FieldExtractor fieldExtractor() {
		BeanWrapperFieldExtractor fieldExtractor = new BeanWrapperFieldExtractor();
		String [] names = new String[]{"id", "name"};
		fieldExtractor.setNames(names);
		return fieldExtractor;
	}

/*	@Bean
	public StepScope stepScope() {
	    final StepScope stepScope = new StepScope();
	    stepScope.setAutoProxy(true);
	    return stepScope;
	}
*/


}
