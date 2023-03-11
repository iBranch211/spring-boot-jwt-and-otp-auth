package com.jwtauth.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;

public abstract class ReportGenerator {

	@Value("${spring.application.name}")
	public String APP_NAME;

	public abstract String getSheetName();

	public abstract List<String> getColumnNames();

	public abstract void fillActualData(Sheet sheet);

	public abstract String getFileName();

	public String generateReport() {

		try (Workbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet(getSheetName());
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 4000);

			Row headerRow = sheet.createRow(0);

			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFFont font = ((XSSFWorkbook) workbook).createFont();
			font.setFontName("Arial");
			font.setFontHeightInPoints((short) 16);
			font.setBold(true);
			headerStyle.setFont(font);

			int cellCounter = 0;

			for (String columnName : getColumnNames()) {
				Cell headerCell = headerRow.createCell(cellCounter++);
				headerCell.setCellValue(columnName);
				headerCell.setCellStyle(headerStyle);
			}

			fillActualData(sheet);

			File tempDir = new File(getTempFilePath());
			if (!tempDir.exists()) {
				tempDir.mkdir();
			}
			String fileLocation = tempDir.getAbsolutePath() + File.separator + getFileName() + "_" + LocalDate.now()
					+ ".xlsx";

			FileOutputStream outputStream = new FileOutputStream(fileLocation);
			workbook.write(outputStream);
			return fileLocation;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTempFilePath() {
		return System.getProperty("java.io.tmpdir") + APP_NAME;
	}

}
