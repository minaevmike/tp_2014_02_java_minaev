
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import frontend.Frontend;
import messages.MessageSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.io.StringWriter;

public class RoutingTest {
    final private static HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    final private static HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    final private static HttpSession httpSession = Mockito.mock(HttpSession.class);
    private static String url;
    final private static StringWriter stringWriter = new StringWriter();
    final private static PrintWriter writer = new PrintWriter(stringWriter);
    private static MessageSystem ms = new MessageSystem();
    final private static Frontend frontend = new Frontend(ms);
    @Before
    public void init() throws Exception{
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
    }
    @Test
    public void testMainPageRouting() throws Exception{
        url = "/";
        Mockito.when(request.getPathInfo()).thenReturn(url);
        Mockito.when(httpSession.getAttribute("userId")).thenReturn(null);
        frontend.doGet(request, response);
        System.out.println(stringWriter);
        Assert.assertTrue(stringWriter.toString().contains("<title>Auth</title>"));
    }
    @Test
    public void testRegistartionPageRouting() throws Exception{
        url = "/registration";
        Mockito.when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("<title>Registartion</title>"));
    }

    @Test
    public void testTimerPageRounting() throws Exception{
        url = "/authform";
        Mockito.when(httpSession.getAttribute("userId")).thenReturn(0L);
        Mockito.when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("<title>Timer</title>"));
    }
}
