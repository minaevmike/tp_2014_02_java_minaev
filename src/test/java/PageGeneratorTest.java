

import templater.PageGenerator;
import org.junit.Test;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import static org.hamcrest.CoreMatchers.containsString;
import java.util.Random;

public class PageGeneratorTest {

    @Test
    public void testMainPage() {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("error", "");
        String file = "authform.tml";
        String page = PageGenerator.getPage(file, pageVariables);
        Assert.assertThat(page, containsString("<form action = \"/authform\" method = \"post\">"));
    }

    @Test
    public void testFailLoginPage(){
        Map<String, Object> pageVariables = new HashMap<>();
        String randomText = RandomStringUtils.randomAlphanumeric(64);
        pageVariables.put("error", randomText);
        String file = "authform.tml";
        String page = PageGenerator.getPage(file, pageVariables);
        Assert.assertThat(page, containsString(randomText));
    }

    @Test
    public void testGoodLoingPage(){
        Random random = new Random();
        Map<String, Object> pageVariables = new HashMap<>();
        //Long userId = random.nextLong();
        String userId = RandomStringUtils.randomAlphanumeric(50);
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", "");
        pageVariables.put("userId", userId);
        String file = "userId.tml";
        String page = PageGenerator.getPage(file, pageVariables);
        Assert.assertThat(page, containsString(userId));
    }
}
