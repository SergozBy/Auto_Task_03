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
}