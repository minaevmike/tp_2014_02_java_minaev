package frontend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import templater.PageGenerator;

/**
 * Created by Mike on 16.02.14.
 */
public class Frontend extends HttpServlet {

    private String login = "";
    private String pass = "";
    private String hardUser = "test";
    private String hardPass = "test";

    private AtomicLong userIdGenerator = new AtomicLong();

    public static String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if(userId != null){
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("serverTime", getTime());
            pageVariables.put("userId", userId);
            response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
            return;
        }
        else {
            pageVariables.put("error", "");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            return;
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        login = request.getParameter("login");
        pass = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        if(login.equals(hardUser) && pass.equals(hardPass)){
            HttpSession session = request.getSession();
            Long userId = (Long) userIdGenerator.getAndIncrement();
            session.setAttribute("userId", userId);
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("serverTime", getTime());
            pageVariables.put("userId", userId);
            response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
            return;
        }
        else{
            pageVariables.put("error", "Wrong password or username");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            return;
        }
    }
}
