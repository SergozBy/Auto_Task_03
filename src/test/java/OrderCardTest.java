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

        String inputSub = driver.findElement(By.cssSelector("[data-test-id='name'] span.input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", inputSub);
    }

    // Negative test without enter phone number
    @Test
    void shouldNotSendFormWithoutPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        // step with enter phone number was missed
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String inputSub = driver.findElement(By.cssSelector("[data-test-id='phone'] span.input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", inputSub);
    }

    // Negative test without select agreement checkbox
    @Test
    void shouldNotSendFormWithoutAgreement() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231234567");
        // step with select agreement checkbox
        driver.findElement(By.cssSelector("button")).click();

        Object classColor = driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__text")).getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", classColor);
    }

    // TASK 2 - OPTIONAL
    // Negative test invalid enter of name
    @Test
    void shouldNotSendFormWithInvalidName() {
        // step with invalid enter name
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("John Travolta");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String classes = driver.findElement(By.cssSelector("[data-test-id='name']")).getAttribute("class");
        boolean haveClassInputInvalid = classes.contains("input_invalid");
        assertEquals(true, haveClassInputInvalid);
    }

    // Negative test invalid enter of phone number #1 - short number
    @Test
    void shouldNotSendFormWithInvalidPhone1() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        // step with enter invalid phone number
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7123");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String classes = driver.findElement(By.cssSelector("[data-test-id='phone']")).getAttribute("class");
        boolean haveClassInputInvalid = classes.contains("input_invalid");
        assertEquals(true, haveClassInputInvalid);
    }

    // Negative test invalid enter of phone number #2 latin letters
    @Test
    void shouldNotSendFormWithInvalidPhone2() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        // step with enter invalid phone number
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("abcdefghikl");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String classes = driver.findElement(By.cssSelector("[data-test-id='phone']")).getAttribute("class");
        boolean haveClassInputInvalid = classes.contains("input_invalid");
        assertEquals(true, haveClassInputInvalid);
    }

    // Negative test invalid enter of phone number #2 cyrillic letters
    @Test
    void shouldNotSendFormWithInvalidPhone3() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        // step with enter invalid phone number
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("абвгдеёжзий");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();

        String classes = driver.findElement(By.cssSelector("[data-test-id='phone']")).getAttribute("class");
        boolean haveClassInputInvalid = classes.contains("input_invalid");
        assertEquals(true, haveClassInputInvalid);
    }

    // Negative test without select agreement checkbox
    @Test
    void shouldNotSendFormWithoutAgreement2() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Траволта");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71231234567");
        // step with select agreement checkbox
        driver.findElement(By.cssSelector("button")).click();

        String classes = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getAttribute("class");
        boolean haveClassInputInvalid = classes.contains("input_invalid");
        assertEquals(true, haveClassInputInvalid);
    }
}