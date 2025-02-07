package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class VehicleOwner extends BasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleOwner.class);

    // Constructor que inicializa el driver y pasa la referencia a la clase base.
    public VehicleOwner(WebDriver driver) {
        super(driver); // Llama al constructor de la clase BasePage para inicializar el WebDriver
    }

    // Localizadores para los elementos de la página de búsqueda de empleo

    private static final By OPTION = By.xpath("//*[@id=\"__next\"]/main/div/div/div/div[2]/a[2]");
    private static final By PLACA_DROPDOWN = By.xpath("//*[@id=\"__next\"]/div/div[5]/div[1]/div/div/p[2]/select");
    private static final By INPUT_PLACA = By.xpath("//*[@id=\"__next\"]/div/div[5]/div[1]/div/div/p[3]/input");
    private static final By SEARCH_BTN = By.xpath("//*[@id=\"__next\"]/div/div[5]/div[2]/button");
    private static final By TABLE_ROWS = By.xpath("//*[@id=\"__next\"]/div/div[5]/div[2]/div[1]/div[1]/div/div[2]/div/table");
    private static final By NEW_SEARCH_BTN = By.xpath("//*[@id=\"__next\"]/div/div[5]/div[2]/div[3]/button");

    public void clickOptionBtn() {
        try {
            click(OPTION);
        } catch (Exception e) {
            LOGGER.error("Error al hacer clic en el botón 'Consultar': " + e.getMessage());
        }
    }

    public void selectPlacaOtion(String tipo) {
        try {
            seleccionarPorTextovisible(PLACA_DROPDOWN, tipo);
        } catch (Exception e) {
            LOGGER.error("Error al seleccionar Tipo '" + tipo + "': " + e.getMessage());
        }
    }

    public void ingresarPlaca(String placa) {
        try {
            type(INPUT_PLACA, placa);
        } catch (Exception e) {
            // Log de error si ocurre una excepción al enviar texto al campo
            LOGGER.error("Error al enviar texto al elemento: " + placa, e.getMessage());
        }
    }

    public void clickSearchBtn() {
        try {
            click(SEARCH_BTN);
        } catch (Exception e) {
            LOGGER.error("Error al hacer clic en el botón 'Consultar': " + e.getMessage());
        }
    }


    public List<List<String>> obtenerDatosVehicleOwner() {
        List<List<String>> dataSearch = new ArrayList<>();
        try {
            List<WebElement> filas = driver.findElements(TABLE_ROWS);
            for (WebElement fila : filas) {
                List<WebElement> celdas = fila.findElements(By.tagName("td"));
                List<String> datosFila = new ArrayList<>();
                for (WebElement celda : celdas) {
                    String textoCelda = celda.getText().trim();

                    datosFila.add(textoCelda);
                }
                if (!datosFila.isEmpty()) {
                    dataSearch.add(datosFila);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error al obtener los datos de la tabla 'valores a pagar': " + e.getMessage());
        }
        return dataSearch;
    }

    public void clickNewSearchBtn() {
        try {
            click(NEW_SEARCH_BTN);
        } catch (Exception e) {
            LOGGER.error("Error al hacer clic en el botón 'Nueva consulta': " + e.getMessage());
        }
    }

}
