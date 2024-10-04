package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider{ // Lets us read the data
        ChromeDriver driver;

        @Test(enabled = true)
        public void testCase01() throws InterruptedException {

                System.out.println("Staring Test Case 01");
                String actualUrl = "https://www.youtube.com/";
                driver.get(actualUrl);
                String currentUrl = driver.getCurrentUrl();
                Assert.assertEquals(currentUrl, actualUrl);
                Wrappers.click(driver, By.linkText("About"));
                Thread.sleep(3000);
                System.out.println(driver.findElement(By.xpath("//section[@class='ytabout__content']")).getText());        

                System.out.println("End Test Case 01");            
        }

        @Test(enabled = true)
        public void testCase02() throws InterruptedException {

                System.out.println("Staring Test Case 02");

                SoftAssert sa = new SoftAssert();
                driver.get("https://www.youtube.com");
                Wrappers.findElementAndClick(driver, By.xpath("//a[@title='Movies']"));
                Thread.sleep(2000);
                Wrappers.elementUnClickable(driver, By.xpath("//span[contains(text(),'Top selling')]//ancestor::div[@id='dismissible']//button[@aria-label='Next']"));
                Thread.sleep(2000);
                By movieRating = By.xpath("//span[contains(text(),'Top selling')]//ancestor::div[@id='dismissible']//div[contains(@class,'badge-style-type-simple')]");
                String rating = Wrappers.findElementAndPrint(driver, movieRating, driver.findElements(movieRating).size()-1);
                sa.assertEquals(rating, 'A');
                By movieCat = By.xpath("//span[contains(text(),'Top selling')]//ancestor::div[@id='dismissible']//span[contains(@class,'grid-movie-renderer-metadata')]");
                String category = Wrappers.findElementAndPrint(driver, movieCat, driver.findElements(movieCat).size()-1);
                sa.assertTrue(category.contains("Animation"));
                
                System.out.println("End Test Case 02");
        }

        @Test(enabled = true)
        public void testCase03() throws InterruptedException {

                System.out.println("Staring Test Case 03");

                SoftAssert sa = new SoftAssert();
                driver.get("https://www.youtube.com");
                Wrappers.findElementAndClick(driver, By.xpath("//a[@title='Music']"));
                Thread.sleep(2000);
                Wrappers.elementUnClickable(driver, By.xpath("//span[contains(text(),'Biggest Hits')]//ancestor::div[@id='dismissible']//button[@aria-label='Next']"));
                By playlist = By.xpath("//span[contains(text(),'Biggest Hits')]//ancestor::div[@id='dismissible']//h3");
                String playlistName = Wrappers.findElementAndPrint(driver, playlist, driver.findElements(playlist).size()-1);
                System.out.println(playlistName);
                By tracks = By.xpath("//span[contains(text(),'Biggest Hits')]//ancestor::div[@id='dismissible']//p[@id='video-count-text']");
                String numberOfTracks = Wrappers.findElementAndPrint(driver, tracks, driver.findElements(tracks).size()-1);
                sa.assertTrue(Wrappers.conversion(numberOfTracks.split(" ")[0])<=50); 

                System.out.println("End Test Case 03");
        }

        @Test(enabled = true)
        public void testCase04() throws InterruptedException {

                System.out.println("Staring Test Case 04");

                WebDriverWait wait;
                long likes = 0;
                driver.get("https://www.youtube.com");
                Wrappers.findElementAndClick(driver, By.xpath("//a[@title='News']"));
                Thread.sleep(5000);
                wait = new WebDriverWait(driver, Duration.ofSeconds(15));

                // Wait for the element to be clickable
                WebElement cards = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Latest')]//ancestor::div[@id='dismissible']//div[contains(@class,'ytd-rich-item-renderer')]")));
                for (int i = 0; i < 3; i++) {

                System.out.println(Wrappers.findCardsAndPrint(driver, By.xpath("//div[@id='author']"), cards, i));
                System.out.println(Wrappers.findCardsAndPrint(driver, By.xpath("//yt-formatted-string[@id='home-content-text']"),cards, i));
                       try {
                              String count = Wrappers.findCardsAndPrint(driver, By.xpath("//span[@id='vote-count-middle']"),cards, i);
                              likes += Wrappers.conversion(count);
                        } catch (NoSuchElementException e) {
                                System.out.println(e.getMessage());
                        }
                        System.out.println(likes);
                        Thread.sleep(3000);
                }
                System.out.println("End Test Case 04");
        }


        @Test(enabled = true, dataProvider = "excelData")
        public void testCase05(String word) throws InterruptedException {

            System.out.println("Starting Test Case 05");

            driver.get("https://www.youtube.com");
            Wrappers.sendKeys(driver, By.xpath("//input[@id='search']"), word);
            Thread.sleep(3000);
            long tally = 0;
            int iter = 1;
            while(tally <= 100000000 && iter > 5) {

                String res =  Wrappers.findElementAndPrint(driver, By.xpath("(//div[@class='style-scope ytd-video-renderer' and @id='meta']//span[@class='inline-metadata-item style-scope ytd-video-meta-block'][1])"),iter);
                res = res.split(" ")[0];
                tally += Wrappers.conversion(res);
                Thread.sleep(3000);
            }
            Thread.sleep(3000);
            System.out.println("End Test Case 05");
        }

        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}