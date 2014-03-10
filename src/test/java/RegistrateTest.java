

import frontend.Frontend;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
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
    @Test
    public void testFailRegistration() throws Exception{
        Frontend frontend = new Frontend();
        url = "/registration";
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
        Mockito.when(request.getParameter("login")).thenReturn("test");
        Mockito.when(request.getParameter("password")).thenReturn("test");
        frontend.doPost(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Already registarted"));
    }
    @Test
    public void testGoodRegistration() throws Exception{
        Frontend frontend = new Frontend();
        url = "/registration";
        String randomUser = RandomStringUtils.randomAlphanumeric(50);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
        Mockito.when(request.getParameter("login")).thenReturn(randomUser);
        Mockito.when(request.getParameter("password")).thenReturn("test");
        frontend.doPost(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Successfully registrated"));
    }
}
