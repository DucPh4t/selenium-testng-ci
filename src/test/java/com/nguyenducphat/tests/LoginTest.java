package com.nguyenducphat.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.nguyenducphat.pages.LoginPage;

import java.time.Duration;

public class LoginTest {
    // Sử dụng ThreadLocal để chạy song song an toàn, không bị đụng độ driver giữa các luồng
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<LoginPage> loginPage = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    public LoginPage getLoginPage() {
        return loginPage.get();
    }

    @BeforeMethod
    public void setup() {
        String browser = System.getProperty("browser", "chrome");
        WebDriver webDriver;

        if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
            webDriver = new FirefoxDriver(options);
        } else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
            webDriver = new ChromeDriver(options);
        }

        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        webDriver.manage().window().maximize();
        webDriver.get("https://sinhvien1.tlu.edu.vn/");

        driver.set(webDriver);
        loginPage.set(new LoginPage(webDriver));
    }

    @Test
    public void testSuccessfulLogin() {
        System.out.println("Thread ID " + Thread.currentThread().getId() + ": Bắt đầu test luồng đăng nhập ĐÚNG mật khẩu...");
        
        // LỰA CHỌN 1: Mật khẩu ĐÚNG (mặc định để test này PASS)
        getLoginPage().enterCredentials("2351067119", "079205011830");

        getLoginPage().clickLogin();

        // Chờ trang xử lý đăng nhập
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(getLoginPage().isLoginSuccessful(), "Đăng nhập thất bại với tài khoản đúng!");
        System.out.println("Thread ID " + Thread.currentThread().getId() + ": Luồng đăng nhập đúng mật khẩu -> THÀNH CÔNG (Pass)");
    }

    @Test
    public void testFailedLogin() {
        System.out.println("Thread ID " + Thread.currentThread().getId() + ": Bắt đầu test luồng đăng nhập SAI mật khẩu...");
        
        // LỰA CHỌN 1: Mật khẩu SAI (mặc định để test này PASS vì mong muốn đăng nhập thất bại)
        getLoginPage().enterCredentials("2351067119", "sai_mat_khau_tlu");

        // LỰA CHỌN 2: Mật khẩu ĐÚNG (bỏ dấu comment '//' dòng dưới và comment dòng Lựa chọn 1 lại nếu thầy yêu cầu "cả 2 acc đều đúng")
        // getLoginPage().enterCredentials("2351067119", "079205011830");

        getLoginPage().clickLogin();

        // Chờ trang xử lý đăng nhập
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Với tài khoản sai, đăng nhập phải thất bại (tức là isLoginSuccessful() trả về false)
        Assert.assertFalse(getLoginPage().isLoginSuccessful(), "Lỗi bảo mật: Đăng nhập sai mật khẩu nhưng vẫn thành công!");
        System.out.println("Thread ID " + Thread.currentThread().getId() + ": Luồng đăng nhập sai mật khẩu -> THẤT BẠI NHƯ MONG ĐỢI (Pass)");
    }

    @AfterMethod
    public void tearDown() {
        WebDriver webDriver = getDriver();
        if (webDriver != null) {
            webDriver.quit();
        }
        driver.remove();
        loginPage.remove();
    }
}
