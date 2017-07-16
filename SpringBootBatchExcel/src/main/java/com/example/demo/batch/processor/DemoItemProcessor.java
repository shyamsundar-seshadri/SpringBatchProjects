package com.example.demo.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.demo.domain.Employee;

public class DemoItemProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee emp) throws Exception {
		System.out.println("In Processoer emp id:" +emp.getId() + " name:"+emp.getName());
		return emp;
	}

}
