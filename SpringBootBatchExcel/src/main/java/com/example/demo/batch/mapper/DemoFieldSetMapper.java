package com.example.demo.batch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.example.demo.domain.Employee;

public class DemoFieldSetMapper implements FieldSetMapper<Employee> {

	@Override
	public Employee mapFieldSet(FieldSet fieldSet) throws BindException {
		Employee emp = new Employee();
		emp.setId(fieldSet.readInt("id"));
		emp.setName(fieldSet.readString("name"));
		return emp;
	}

}
