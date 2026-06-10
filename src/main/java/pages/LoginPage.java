package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[contains(text(), 'Đăng nhập')]")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterCredentials(String username, String password) {
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        loginButton.click();
    }

    public boolean isLoginSuccessful() {
        try {
            // Chờ tối đa 3 giây để kiểm tra xem nút Đăng nhập còn tồn tại không
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
            boolean buttonExists = driver.findElements(By.xpath("//button[contains(text(), 'Đăng nhập')]")).size() > 0;
            return !buttonExists;
        } catch (Exception e) {
            return true; // Nếu lỗi xảy ra, có thể đã đăng nhập thành công và trang bị chuyển đổi
        } finally {
            // Khôi phục lại implicit wait chuẩn là 15 giây
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        }
    }
}
