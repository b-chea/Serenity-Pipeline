package runner;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.utils.ExcelHelper;
import org.example.utils.ExcelReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import steps.VehicleOwnerSteps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SerenityRunner.class)
public class VehicleOwnerTest {
    @Steps
    private VehicleOwnerSteps vehicleOwnerSteps;

    // Define y gestiona el WebDriver para el navegador Chrome
    @Managed(driver = "chrome")
    private WebDriver driver;

    // URL base para la aplicación a probar
    private static final String BASE_URL = "https://consultasecuador.com/en-linea";

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleOwnerTest.class);

    // Ruta al archivo de datos de prueba Excel
    private static final String EXCEL_PATH = "src/main/resources/test-data/data.xlsx";

    // Nombre de la hoja en el archivo Excel para los datos
    private static final String SHEET_NAME = "Placas";

    private ExcelReader excelReader;
    private Workbook workbook;

    /**
     * Configuración inicial para la prueba.
     * Se inicializa el lector de Excel antes de la ejecución de los casos de prueba.
     *
     * @throws IOException Si ocurre un error al leer el archivo Excel.
     */
    @Before
    public void setup() throws IOException {
        // Inicializar el lector de Excel
        excelReader = new ExcelReader(EXCEL_PATH);
        LOGGER.info("Lector de Excel inicializado correctamente.");
    }

    @Test
    public void testVehicleOwner() throws IOException, InterruptedException {

        // Pasar el WebDriver a los pasos
        vehicleOwnerSteps.setDriver(driver);

        // Abre la página web
        vehicleOwnerSteps.openPage(BASE_URL);

        vehicleOwnerSteps.clickoptionButton();

        // Leer los datos del Excel
        List<Map<String, String>> data = excelReader.getSheetData(SHEET_NAME);
        List<List<String>> resultadosConsultas = new ArrayList<>();  // Lista para acumular resultados

        // Iterar sobre las filas y aplicar los filtros
        for (Map<String, String> row : data) {
            String tipo = row.getOrDefault("Tipo", "");
            String placa = row.getOrDefault("Placas", "");


            // Validar que los datos no estén vacíos
            if (placa.isEmpty()|| tipo.isEmpty()) {
                LOGGER.warn("Datos incompletos en la fila: {}", row);
                continue;
            }
            vehicleOwnerSteps.selectPlacaOtion(tipo);
            vehicleOwnerSteps.searchByPlaca(placa);
            vehicleOwnerSteps.search();
            vehicleOwnerSteps.collectResults();
            vehicleOwnerSteps.newSearch();
            // Validar resultados
            LOGGER.info("Prueba completada con filtros: Placa={}, Tipo={}",
                    placa, tipo);

        }
        vehicleOwnerSteps.saveExcel();
        driver.quit();

    }

}
