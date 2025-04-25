package com.employee.utils;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.employee.domain.Employee;


public class DocumentHelper {
	
	public static byte[] DownloadExcelFromDatabase(List<Employee> listOfEmp) {
		
		final String sheetName = "employee_data";
		final String[] headers = {"Employee Id", "Name", "Dob", "Email", "Username", "Gender", "Address", "Contact", "PinCode"};
		
		try {
			Workbook workbook = new XSSFWorkbook();
			
			Sheet sheet = workbook.createSheet(sheetName);
			
			Row firstRow = sheet.createRow(0);
			firstRow.createCell(0).setCellValue(headers[0]);
			firstRow.createCell(1).setCellValue(headers[1]);
			firstRow.createCell(2).setCellValue(headers[2]);
			firstRow.createCell(3).setCellValue(headers[3]);
			firstRow.createCell(4).setCellValue(headers[4]);
			firstRow.createCell(5).setCellValue(headers[5]);
			firstRow.createCell(6).setCellValue(headers[6]);
			firstRow.createCell(7).setCellValue(headers[7]);
			firstRow.createCell(8).setCellValue(headers[8]);
			
			Integer rowCount = 1;
			for(Employee emp: listOfEmp) {
				Row row = sheet.createRow(rowCount);
				row.createCell(0).setCellValue(emp.getId());
				row.createCell(1).setCellValue(emp.getName());
				row.createCell(2).setCellValue(emp.getDob());
				row.createCell(3).setCellValue(emp.getEmail());
				row.createCell(4).setCellValue(emp.getUsername());
				row.createCell(5).setCellValue(emp.getGender().toString());
				row.createCell(6).setCellValue(emp.getAddress());
				row.createCell(7).setCellValue(emp.getContactNumber());
				row.createCell(8).setCellValue(emp.getPinCode());
				rowCount++;
			}
			
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			workbook.write(bOut);
			
			return bOut.toByteArray();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
