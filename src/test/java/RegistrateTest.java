

import databaseservice.DatabaseService;
import frontend.Frontend;
import messages.MessageSystem;
import messages.TimeHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

public class RegistrateTest {
    final private static HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    final private static HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    final private static HttpSession httpSession = Mockito.mock(HttpSession.class);
    private static String url;
    final private static StringWriter stringWriter = new StringWriter();
    final private static PrintWriter writer = new PrintWriter(stringWriter);
    private static MessageSystem ms = new MessageSystem();
    private static DatabaseService databaseService = new DatabaseService(ms);
    final private static Frontend frontend = new Frontend(ms);
    final  private static Long timeToSleep = 6000L;
    @Before
    public void init() throws Exception{
        url = "/registration";
        (new Thread(frontend)).start();
        (new Thread(databaseService)).start();
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
    }
    @Test
    public void testFailRegistration() throws Exception{
        Mockito.when(request.getParameter("login")).thenReturn("test");
        Mockito.when(request.getParameter("password")).thenReturn("test");
        frontend.doPost(request, response);
        Thread.sleep(timeToSleep);
        Mockito.when(request.getPathInfo()).thenReturn("/reging");
        frontend.doGet(request, response);
        System.out.println(stringWriter);
        Assert.assertTrue(stringWriter.toString().contains("Duplicate username"));
    }
    @Test
    public void testGoodRegistration() throws Exception{
        String randomUser = RandomStringUtils.randomAlphanumeric(50);
        Mockito.when(request.getParameter("login")).thenReturn(randomUser);
        Mockito.when(request.getParameter("password")).thenReturn("test");
        frontend.doPost(request, response);
        Thread.sleep(timeToSleep);
        Mockito.when(request.getPathInfo()).thenReturn("/reging");
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Successfully registrated"));
    }
}
