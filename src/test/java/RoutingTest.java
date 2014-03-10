
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import frontend.Frontend;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.io.StringWriter;

public class RoutingTest {
    final private static HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    final private static HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    final private static HttpSession httpSession = Mockito.mock(HttpSession.class);
    private static String url;
    @Test
    public void testMainPageRouting() throws Exception{
        Frontend frontend = new Frontend();
        url = "/";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
        Mockito.when(httpSession.getAttribute("userId")).thenReturn(null);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("<title>Auth</title>"));
    }
    @Test
    public void testRegistartionPageRouting() throws Exception{
        Frontend frontend = new Frontend();
        url = "/registration";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("<title>Registartion</title>"));
    }

    @Test
    public void testTimerPageRounting() throws Exception{
        Frontend frontend = new Frontend();
        url = "/authform";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(httpSession.getAttribute("userId")).thenReturn(0L);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
        frontend.doGet(request, response);
        Assert.assertTrue(stringWriter.toString().contains("<title>Timer</title>"));
    }
}
