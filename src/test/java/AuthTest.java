
import databaseservice.DatabaseService;
import frontend.Frontend;
import messages.AddressService;
import messages.MessageSystem;
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

public class AuthTest {
    final private static HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    final private static HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    final private static HttpSession httpSession = Mockito.mock(HttpSession.class);
    private static String url;
    private static MessageSystem ms = new MessageSystem();
    private static Frontend frontend = new Frontend(ms);
    private static StringWriter stringWriter = new StringWriter();
    private static PrintWriter writer = new PrintWriter(stringWriter);
    final private static Long timeToWait = 6000L;
    private static DatabaseService databaseService = new DatabaseService(ms);
    String login = "test";

    @Before
    public void init() throws Exception{
        url = "/authform";
        (new Thread(frontend)).start();
        (new Thread(databaseService)).start();
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
        Mockito.when(request.getSession().getId()).thenReturn("testSession");
    }
    @Test
    public void testFailAuth() throws Exception{
        Mockito.when(request.getParameter("login")).thenReturn(login);
        Mockito.when(request.getParameter("password")).thenReturn("wrongpass");
        frontend.doPost(request, response);
        Thread.sleep(timeToWait);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Wrong password"));
    }

    @Test
    public void testGoodAuth() throws Exception{
        Mockito.when(request.getParameter("login")).thenReturn(login);
        Mockito.when(request.getParameter("password")).thenReturn("test");
        frontend.doPost(request, response);
        Thread.sleep(timeToWait);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("You login name"));
    }

    @Test
    public void testNoSuchUser() throws  Exception{
        Mockito.when(request.getParameter("login")).thenReturn(RandomStringUtils.randomAlphanumeric(50));
        Mockito.when(request.getParameter("password")).thenReturn("test");
        frontend.doPost(request, response);
        Thread.sleep(timeToWait);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("No such user"));
    }
}
