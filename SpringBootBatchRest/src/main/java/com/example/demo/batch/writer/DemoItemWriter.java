package com.example.demo.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.example.demo.domain.Employee;

public class DemoItemWriter implements ItemWriter<Employee> {

	@Override
	public void write(List<? extends Employee> empList) throws Exception {
		
		for(Employee emp : empList) {
			System.out.println("name: "+emp.getName()+" id:"+emp.getId());
		}
	}

}
