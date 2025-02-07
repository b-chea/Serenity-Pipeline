package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase base para las páginas de la aplicación.
 * Esta clase contiene métodos comunes de interacción con los elementos de la interfaz de usuario.
 * Se utilizan técnicas como excepciones y logging para mejorar el monitoreo y manejo de errores.
 */
public abstract class BasePage {
    // Driver utilizado para interactuar con el navegador
    protected WebDriver driver;

    // Logger para la clase BasePage, utilizado para registrar información y errores
    private static final Logger LOGGER = Logger.getLogger(BasePage.class.getName());


    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public void click(By locator) {
        try {
            driver.findElement(locator).click();
            // Log de éxito al hacer clic en el elemento
            LOGGER.info("Elemento clickeado con éxito: " + locator);
        } catch (Exception e) {
            // Log de error si ocurre una excepción al intentar hacer clic en el elemento
            LOGGER.log(Level.SEVERE, "Error al hacer clic en el elemento: " + locator, e);
        }
    }

    public void type(By locator, String text) {
        try {
            driver.findElement(locator).sendKeys(text);
            // Log de éxito al enviar texto al elemento
            LOGGER.info("Texto enviado exitosamente al elemento: " + locator);
        } catch (Exception e) {
            // Log de error si ocurre una excepción al enviar texto al campo
            LOGGER.log(Level.SEVERE, "Error al enviar texto al elemento: " + locator, e);
        }
    }

    public String getText(By locator) {
        try {
            // Espera explícita para asegurar que el elemento esté presente
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            String text = element.getText().trim();
            // Log de éxito al obtener el texto del elemento
            LOGGER.info("Texto obtenido con éxito del elemento: " + locator);
            return text;
        } catch (Exception e) {
            // Log de error si ocurre una excepción al obtener el texto
            LOGGER.log(Level.SEVERE, "Error al obtener el texto del elemento: " + locator, e);
            return "No disponible"; // Valor por defecto si ocurre un error
        }
    }

    public void seleccionarPorIndice(By locator, int index) {
        try {
            WebElement element = driver.findElement(locator);
            Select dropdown = new Select(element);
            dropdown.selectByIndex(index);
            // Log de éxito al seleccionar por índice
            LOGGER.info("Opción seleccionada por índice en el elemento: " + locator + ", índice: " + index);
        } catch (Exception e) {
            // Log de error si ocurre una excepción al seleccionar por índice
            LOGGER.log(Level.SEVERE, "Error al seleccionar por índice en el elemento: " + locator + ", índice: " + index, e);
        }
    }

    public void seleccionarPorTextovisible(By locator, String visibleText) {
        try {
            // Espera explícita para asegurarse de que el elemento esté visible antes de interactuar con él
            WebDriverWait wait = new WebDriverWait(driver, 20);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            // Una vez visible, selecciona la opción por texto visible
            Select dropdown = new Select(element);
            dropdown.selectByVisibleText(visibleText);

            // Log de éxito al seleccionar por texto visible
            LOGGER.info("Opción seleccionada por texto visible en el elemento: " + locator + ", texto: " + visibleText);
        } catch (Exception e) {
            // Log de error si ocurre una excepción al seleccionar por texto visible
            LOGGER.log(Level.SEVERE, "Error al seleccionar por texto visible en el elemento: " + locator + ", texto: " + visibleText, e);
        }
    }
}
