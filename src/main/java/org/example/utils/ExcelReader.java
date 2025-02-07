package org.example.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    private Workbook workbook;
    private String filePath;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

    //constructor
    public ExcelReader(String filePath) {
        this.filePath = filePath;//ruta

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(inputStream);
            LOGGER.info("Archivo Excel cargado correctamente: {}", filePath);
        } catch (IOException e) {
            LOGGER.error("Error al leer el archivo Excel: {}", filePath, e);
            throw new RuntimeException("Error al leer el archivo Excel: " + filePath, e);
        }
    }

    public List<Map<String, String>> getSheetData(String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet != null) {
                LOGGER.info("Procesando hoja: {}", sheetName);
                Row header = sheet.getRow(0);
                if (header == null) {
                    LOGGER.warn("La hoja {} no tiene encabezados en la primera fila.", sheetName);
                    throw new IllegalArgumentException("La hoja no tiene encabezados en la primera fila.");
                }

                for (int r = 1; r <= sheet.getLastRowNum(); r++) { // Recorrer filas
                    Row row = sheet.getRow(r);
                    if (row == null || isRowBlank(row)) continue;

                    Map<String, String> data = new HashMap<>();
                    for (int c = 0; c < header.getLastCellNum(); c++) {
                        Cell headerCell = header.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                        // Validación si la celda contiene datos
                        String key = headerCell == null ? "Columna" + c : headerCell.toString();
                        String value = getCellValueAsString(cell);
                        if (!value.isEmpty()) {
                            data.put(key, value);
                        }
                    }
                    if (!data.isEmpty()) {
                        LOGGER.debug("Fila procesada: {}", data);
                        System.out.println("Fila procesada: " + data);
                        dataList.add(data);
                    }
                }
            } else {
                LOGGER.error("No se encontró la hoja con nombre: {}", sheetName);
                throw new IllegalArgumentException("No se encontró la hoja con nombre: " + sheetName);
            }
            return dataList;
        }catch (Exception e) {
            throw new RuntimeException("Error al procesar los datos de la hoja: " + sheetName, e);
        }

    }

    private boolean isRowBlank(Row row) {
        for (int c = 0; c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell != null && !cell.toString().trim().isEmpty()) {
                return false; // Si se encuentra algún dato en la fila, no está en blanco
            }
        }
        LOGGER.debug("Fila {} está vacía o es nula.", row.getRowNum());
        return true; // Si no hay datos, la fila está completamente en blanco
    }

    // Metodo que convierte cualquier tipo de celda a String
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Si es una fecha, formateamos a String
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();

                    // Si el valor numérico es un entero (sin decimales), lo convertimos a entero
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue); // Convertir a long (entero)
                    } else {
                        return String.valueOf(numericValue); // Si tiene decimales, lo dejamos como decimal
                    }
                }
            case STRING:
                // Si es una celda de texto, simplemente devolvemos el valor
                return cell.getStringCellValue();
            case BOOLEAN:
                // Si es un valor booleano, lo convertimos a String
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Si es una celda con fórmula, obtenemos su valor evaluado
                return cell.getCellFormula();
            default:
                // Si el tipo es desconocido o no manejado, devolvemos un vacío
                return "";
        }
    }



    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
            LOGGER.info("Archivo Excel cerrado.");
        }
    }
}