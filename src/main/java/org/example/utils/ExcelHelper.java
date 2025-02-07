package org.example.utils;

import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelHelper {
    // Crear el Logger para la clase
    private static final Logger LOGGER = Logger.getLogger(ExcelHelper.class.getName());

    public static void crearEncabezado(Sheet sheet, List<String> encabezado) {
        try {
            Row row = sheet.createRow(0);
            for (int i = 0; i < encabezado.size(); i++) {
                row.createCell(i).setCellValue(encabezado.get(i));
            }
            // Log de éxito
            LOGGER.info("Encabezado creado exitosamente.");
        } catch (Exception e) {
            // Log de error
            LOGGER.log(Level.SEVERE, "Error al crear el encabezado.", e);
        }
    }

    public static void agregarFila(Sheet sheet, int rowIndex, List<String> datosFila) {
        try {
            Row row = sheet.createRow(rowIndex);
            for (int i = 0; i < datosFila.size(); i++) {
                row.createCell(i).setCellValue(datosFila.get(i));
            }
            // Log de éxito
            LOGGER.info("Fila agregada exitosamente en la fila " + rowIndex);
        } catch (Exception e) {
            // Log de error
            LOGGER.log(Level.SEVERE, "Error al agregar la fila en la posición " + rowIndex, e);
        }
    }

    public static void guardarArchivo(Workbook workbook, String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            // Log de éxito
            LOGGER.info("Archivo guardado exitosamente en: " + filePath);
        } catch (IOException e) {
            // Log de error
            LOGGER.log(Level.SEVERE, "Error al guardar el archivo en la ruta: " + filePath, e);
        }
    }
}
