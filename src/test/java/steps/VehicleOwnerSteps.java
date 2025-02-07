package steps;

import net.thucydides.core.annotations.Step;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.pages.VehicleOwner;
import org.example.utils.ExcelHelper;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.ExcelDataService;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class VehicleOwnerSteps {
    private WebDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleOwnerSteps.class);
    private VehicleOwner vehicleOwner;
    private Sheet sheet;

    /**
     * Constructor de la clase JobSearchSteps.
     * Inicializa la página de búsqueda de empleo sin el WebDriver en este paso.
     */
    public VehicleOwnerSteps() {
        this.vehicleOwner = new VehicleOwner(driver); // Inicializa la página asociada
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver; // Asigna el WebDriver a la variable local
        this.vehicleOwner = new VehicleOwner(driver); // Inicializa la página asociada con el mismo WebDriver
    }

    @Step
    public void openPage(String url) {
        driver.get(url); // Navega a la URL proporcionada
        driver.manage().window().maximize(); // Maximiza la ventana del navegador
    }
    @Step
    public void clickoptionButton() throws InterruptedException {
        try {
            Thread.sleep(5000);
            vehicleOwner.clickOptionBtn();

        } catch (Exception e) {

        }
    }
    @Step
    public void selectPlacaOtion(String tipo) {
        vehicleOwner.selectPlacaOtion(tipo);

    }

    @Step
    public void searchByPlaca(String placa) throws InterruptedException {
        try {
            vehicleOwner.ingresarPlaca(placa);

        } catch (Exception e) {

        }
    }
    @Step
    public void search() throws InterruptedException {
        try {
            Thread.sleep(5000);
            vehicleOwner.clickSearchBtn();

        } catch (Exception e) {

        }
    }

    @Step
    public void collectResults() {
        // Obtener los datos de ambas tablas
        List<List<String>> dataSearchVehicleOwner = vehicleOwner.obtenerDatosVehicleOwner();

        ExcelDataService.collectResultsAndSave(dataSearchVehicleOwner);
        //ExcelDataService.saveExcel();

    }
    public void saveExcel() {
        // Obtener los datos de ambas tablas
        List<List<String>> dataSearchVehicleOwner = vehicleOwner.obtenerDatosVehicleOwner();

        // Llamar al método para agregar los resultados al archivo Excel
        ExcelDataService.collectResultsAndSave(dataSearchVehicleOwner);

        // Guardar el archivo Excel después de haber agregado los datos
        ExcelDataService.saveExcel();

    }

    @Step
    public void newSearch() throws InterruptedException {
        try {
            Thread.sleep(5000);
            vehicleOwner.clickNewSearchBtn();
            Thread.sleep(5000);

        } catch (Exception e) {

        }
    }

}
