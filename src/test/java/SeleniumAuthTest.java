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
    private static final String url = "http://localhost:8080";
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
        Boolean isTimer = (new WebDriverWait(driver, 4)).until(new ExpectedCondition<Boolean>() {
            @Override
            @NotNull
            public Boolean apply(@NotNull WebDriver d) {
                final String id = (d.findElement(By.id("userId")).getText());
                return id.contains("You login name");
            }
        });
        Assert.assertTrue(isTimer);
        driver.quit();
    }
    @Test//(expected = org.openqa.selenium.TimeoutException.class)
    public void testFailLogin(){
        password = "test213";
        login = "test";
        sendAndSubmitData();
        Boolean isTimer = (new WebDriverWait(driver, 7)).until(new ExpectedCondition<Boolean>() {
            @Override
            @NotNull
            public Boolean apply(@NotNull WebDriver d) {
                System.out.println(d.getPageSource());
                final String id = (d.findElement(By.id("userId")).getText());
                return id.contains("Wrong password");
            }
        });
        Assert.assertTrue(isTimer);
        driver.quit();
    }


}
