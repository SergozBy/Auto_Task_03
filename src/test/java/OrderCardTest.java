import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCardTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    // TASK 1

    // Positive test with valid data
    @Test
    void shouldSendForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String alert = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", alert);
    }

    // Negative test without enter name
    @Test
    void shouldNotSendFormWithoutName() {
        // step with enter name was missed
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String inputSub = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", inputSub);
    }

    // Negative test without enter phone number
    @Test
    void shouldNotSendFormWithoutPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        // step with enter phone number was missed
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String inputSub = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", inputSub);
    }

    // Negative test without select agreement checkbox
    @Test
    void shouldNotSendFormWithoutAgreement() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231234567");
        // step with select agreement checkbox
        driver.findElement(By.cssSelector("button")).click();

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed());
    }

    // TASK 2 - OPTIONAL
    // Negative test invalid enter of name
    @Test
    void shouldInformAboutInvalidName() {
        // step with invalid enter name
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("John Travolta");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String inputSub = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", inputSub);
    }

    // Negative test invalid enter of phone number #1 - short number
    @Test
    void shouldInformAboutInvalidPhone1() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        // step with enter invalid phone number
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7123");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String inputSub = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", inputSub);
    }

    // Negative test invalid enter of phone number #2 latin letters
    @Test
    void shouldInformAboutInvalidPhone2() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        // step with enter invalid phone number
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("abcdefghikl");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String inputSub = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", inputSub);
    }

    // Negative test invalid enter of phone number #3 cyrillic letters
    @Test
    void shouldInformAboutInvalidPhone3() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        // step with enter invalid phone number
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("абвгдеёжзий");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String inputSub = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", inputSub);
    }

    // Negative test without select agreement checkbox
    // Duplicate of shouldNotSendFormWithoutAgreement
    @Test
    void shouldInformAboutWithoutAgreement() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231234567");
        // step with select agreement checkbox
        driver.findElement(By.cssSelector("button")).click();

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed());
    }
}