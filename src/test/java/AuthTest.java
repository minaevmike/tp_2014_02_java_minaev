
import frontend.Frontend;
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
    final private static Frontend frontend = new Frontend();
    private static StringWriter stringWriter = new StringWriter();
    private static PrintWriter writer = new PrintWriter(stringWriter);
    @Before
    public void init() throws Exception{
        url = "/authform";
        Mockito.when(response.getWriter()).thenReturn(writer);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getPathInfo()).thenReturn(url);
        Mockito.when(request.getParameter("login")).thenReturn("test");
    }
    @Test
    public void testFailAuth() throws Exception{
        Mockito.when(request.getParameter("password")).thenReturn("wrongpass");
        frontend.doPost(request, response);
        Assert.assertTrue(stringWriter.toString().contains("Wrong password or username"));
    }

    @Test
    public void testGoodAuth() throws Exception{
        Mockito.when(request.getParameter("password")).thenReturn("test");
        frontend.doPost(request, response);
        Assert.assertTrue(stringWriter.toString().contains("<p>Client time: <span id='ClientTime'></span></p>"));
    }
}
