package com.example.demo.batch.writer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.example.demo.domain.Employee;

public class ExcelFileItemWriter extends AbstractItemStreamItemWriter<Employee> implements ResourceAwareItemWriterItemStream<Employee>, InitializingBean{
	private List<List> metaData;
	private String styleSheetLocation;
	private String outputFileLocation;
	private Integer rownum;

	@Override
	public void write(List<? extends Employee> empList) throws Exception {
		 XSSFWorkbook workbook;
		 FileOutputStream outputStream; 
		 XSSFSheet sheet;
//		Object[][] datatypes = {{}};
		outputStream = new FileOutputStream(outputFileLocation, true);
		 workbook = new XSSFWorkbook();
		 sheet =  workbook.createSheet();
	        /*Object[][] datatypes = {
	                {"Datatype", "Type", "Size(in bytes)"},
	                {"int", "Primitive", 2},
	                {"float", "Primitive", 4},
	                {"double", "Primitive", 8},
	                {"char", "Primitive", 1},
	                {"String", "Non-Primitive", "No fixed size"}
	        };*/
	        XSSFCellStyle style = workbook.createCellStyle();
	        XSSFFont font = workbook.createFont();
	        XSSFColor color = new XSSFColor();
	        color.setIndexed(55);
	        font.setBold(true);
	        style.setFont(font);
	        style.setFillBackgroundColor (color);
	        Object [] [] styleType = {{style,null,null}};
	        System.out.println("Creating excel");
	        System.out.println("rownum is "+rownum);
	        for(Object row : empList) {
	        	Row excelRow = sheet.createRow(rownum++);
	        	Field[] elements = row.getClass().getDeclaredFields();
	        	int colNum = 0;
	        	for(Field element : elements) {
	        		element.setAccessible(true);
	        		 Cell cell = excelRow.createCell(colNum++);
		                if (element.get(row) instanceof String) {
		                	System.out.println(element.get(row));
		                    cell.setCellValue((String) element.get(row) );
		                    cell.setCellStyle(style);
		                } else if (element.get(row) instanceof Integer) {
		                	System.out.println(element.get(row));
		                    cell.setCellValue((Integer) element.get(row) );
		                }
	        	}
	        }
	        /*for (Object[] datatype : datatypes) {
	            Row row = sheet.createRow(rowNum++);
	            int colNum = 0;
	            for (Object field : datatype) {
	                Cell cell = row.createCell(colNum++);
	                if (field instanceof String) {
	                    cell.setCellValue((String) field);
	                    cell.setCellStyle(style);
	                } else if (field instanceof Integer) {
	                    cell.setCellValue((Integer) field);
	                }
	            }
	        }*/

	        try {
	        	
	            workbook.write(outputStream);
	            workbook.close();
//	            outputStream.close();
//	            outputStream.flush();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        System.out.println("Done");
	}
	
	@Override
	public void open(ExecutionContext executionContext) {
		super.open(executionContext);
	}
	
	@Override
	public void close() {
		super.close();
		/*try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	
	@Override
	public void setResource(Resource resource) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	

	public void setOutputFileLocation(String outputFileLocation) {
		this.outputFileLocation = outputFileLocation;
	}
	public void setRownum(Integer rownum) {
		this.rownum = rownum;
	}

}
