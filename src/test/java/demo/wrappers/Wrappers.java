package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {

    public static void sendKeys(WebDriver driver, By locator, String text) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(text);
            element.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            System.out.println("Exception Occured! " + e.getMessage());
        }
    }
    
    public static void click(WebDriver driver, By locator) {
        try {
          WebElement ele = driver.findElement(locator);
          JavascriptExecutor js = (JavascriptExecutor)driver;
          js.executeScript("arguments[0].scrollIntoView();", ele);
          ele.click();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void findElementAndClick(WebDriver driver, By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // wait till element is clickable
            WebElement ele = wait.until(ExpectedConditions.elementToBeClickable(locator));

            // wait until element is visible 
            wait.until(ExpectedConditions.visibilityOf(ele));

            // click the element
            ele.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String findCardsAndPrint(WebDriver driver, By locator, WebElement parent, int n) {

        WebElement element = parent.findElements(locator).get(n);
        return element.getText(); 
    }

    public static void elementUnClickable(WebDriver driver, By locator) {
        WebElement ele = driver.findElement(locator);
        while(ele.isDisplayed()) {
            try {
                findElementAndClick(driver, locator);
            } catch (TimeoutException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    public static String findElementAndPrint(WebDriver driver, By locator, int number) {
    
            WebElement element = driver.findElements(locator).get(number);
            String text = element.getText();
            return text;
        }

    public static long conversion(String value) {
        // Trim the string to remove any leading or trailing spaces
        value = value.trim().toUpperCase();

        // Check if the last character is non-numeric and determine the multiplier
        char lastChar = value.charAt(value.length()-1);
        int multiplier = 1;
        switch (lastChar) {
            case 'K':
                multiplier = 1000;
                break;
            case 'M':
                multiplier = 1000000;
                break;
            case 'B':
                multiplier = 1000000000;
                break;       
            default:
                 // If the last character is numeric, parse the entire string
                 if(Character.isDigit(lastChar))
                    return Long.parseLong(value);

                throw new IllegalArgumentException("Invalid format: " + value);    
        }
        // Extract the numeric part before the last character
        String numericPart = value.substring(0, value.length() - 1);
        double number = Double.parseDouble(numericPart);

        // Calculate the final value
        return (long) (number * multiplier);
        }    
    }

