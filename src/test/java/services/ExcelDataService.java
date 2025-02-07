package services;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.utils.ExcelHelper;

import java.util.List;

public class ExcelDataService {
    // Manejamos el workbook globalmente para que se pueda usar en otros métodos
    private static Workbook workbook = new XSSFWorkbook();

    public static void collectResultsAndSave(List<List<String>> dataSearchVehicleOwner) {

        // Comprobar si la hoja "ResultsVehicleOwner" ya existe
        Sheet sheet1 = workbook.getSheet("ResultsVehicleOwner");

        // Si no existe, la creamos
        if (sheet1 == null) {
            sheet1 = workbook.createSheet("ResultsVehicleOwner");
        }

        // Definir los encabezados para cada hoja
        List<String> encabezadoVehicleOwner = List.of("Marca", "Modelo", "Año de modelo", "País");


        // Crear el encabezado en ambas hojas
        ExcelHelper.crearEncabezado(sheet1, encabezadoVehicleOwner);

        // Agregar los datos a la hoja "ResultsVehicleOwner"
        int rowIndex1 = sheet1.getLastRowNum() + 1; // Aseguramos que los datos se agreguen después de la última fila existente

        for (List<String> dataResults : dataSearchVehicleOwner) {
            Row row = sheet1.createRow(rowIndex1);
            for (int i = 0; i < dataResults.size(); i++) {
                row.createCell(i).setCellValue(dataResults.get(i)); // Agregar cada dato en la celda correspondiente
            }
            rowIndex1++; // Aumentamos el índice de fila después de cada conjunto de datos
        }

    }
    // Método para guardar el archivo Excel
    public static void saveExcel() {
        try {
            // Verificamos que el workbook no esté vacío antes de intentar guardarlo
            if (workbook != null) {
                ExcelHelper.guardarArchivo(workbook, "src/main/resources/outputs/search_results.xlsx");
                System.out.println("Archivo Excel guardado exitosamente.");
            } else {
                System.out.println("No hay datos para guardar.");
            }
        } catch (Exception e) {
            System.err.println("Error al guardar el archivo Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
