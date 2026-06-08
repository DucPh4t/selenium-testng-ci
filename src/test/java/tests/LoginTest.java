package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

import java.time.Duration;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        String browser = System.getProperty("browser", "chrome");

        if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("-headless");
            driver = new FirefoxDriver(options);
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            driver = new ChromeDriver(options);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testSuccessfulLogin() {
        loginPage.enterCredentials("standard_user", "secret_sauce");
        loginPage.clickLogin();

        // Thêm delay nhỏ để web kịp chuyển trang
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login failed with correct credentials!");
    }

    @Test
    public void testFailedLogin() {
        System.out.println("Bắt đầu test luồng đăng nhập SAI mật khẩu...");
        // Cố tình nhập mật khẩu sai
        loginPage.enterCredentials("standard_user", "sai_mat_khau_roi");
        loginPage.clickLogin();

        // Thêm delay nhỏ để web kịp xử lý
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Đăng nhập sai thì isLoginSuccessful() phải trả về false.
        // Ta dùng assertFalse, nếu nó trả về false -> Test Pass (vì đúng kịch bản là không cho đăng nhập)
        Assert.assertFalse(loginPage.isLoginSuccessful(), "Lỗi bảo mật: Đăng nhập sai pass nhưng vẫn thành công!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
