import com.sun.istack.internal.NotNull;
import org.apache.xpath.operations.Bool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeoutException;

public class SeleniumAuthTest {
    private static final String url = "http://localhost:8080/authform";
    private static String password;
    private static String login;
    private static final WebDriver driver = new HtmlUnitDriver(true);
    public void sendAndSubmitData(){
        driver.findElement(By.name("login")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("password")).submit();
    }
    @Before
    public void init(){
        driver.get(url);
    }
    @Test
    public void testGoodLogin(){
        password = "test";
        login = "test";
        sendAndSubmitData();
        Boolean isTimer = (new WebDriverWait(driver, 2)).until(new ExpectedCondition<Boolean>() {
            @Override
            @NotNull
            public Boolean apply(@NotNull WebDriver d) {
                final String id = (d.findElement(By.tagName("title")).getText());
                return id.contains("Timer");
            }
        });
        Assert.assertTrue(isTimer);
        driver.quit();
    }
    @Test(expected = org.openqa.selenium.TimeoutException.class)
    public void testFailLogin(){
        password = "test";
        login = "test123";
        sendAndSubmitData();
        Boolean isTimer = (new WebDriverWait(driver, 2)).until(new ExpectedCondition<Boolean>() {
            @Override
            @NotNull
            public Boolean apply(@NotNull WebDriver d) {
                final String id = (d.findElement(By.tagName("title")).getText());
                return id.contains("Wrong password or username");
            }
        });
        Assert.assertTrue(isTimer);
        driver.quit();
    }


}
