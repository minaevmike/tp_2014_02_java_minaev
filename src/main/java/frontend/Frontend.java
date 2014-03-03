package frontend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import templater.PageGenerator;
import logic.user;
import DAO.Factory;

public class Frontend extends HttpServlet {

    private static final String hardUser1 = "test1";
    private static final String hardPass1 = "test1";
    private static final String hardUser2 = "test2";
    private static final String hardPass2 = "test2";
    private static DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private AtomicLong userIdGenerator = new AtomicLong();

    public static String getTime() {
        return formatter.format(new Date());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        if(request.getPathInfo().equals("/authform")){
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null){
                pageVariables.put("error", "");
                response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            }
            else {
                pageVariables.put("refreshPeriod", "1000");
                pageVariables.put("serverTime", getTime());
                pageVariables.put("userId", userId);
                response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
            }
        }
        else
        if(request.getPathInfo().equals("/registration")){
            pageVariables.put("error", "");
            response.getWriter().println(PageGenerator.getPage("registration.tml", pageVariables));
        }
        else{
            pageVariables.put("error", "");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        if(request.getPathInfo().equals("/authform")){
            user u = null;
            try {
                u = Factory.getInstance().getUserDAO().getUserByName(login);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(u != null && u.getPass().equals(pass)) {
                HttpSession session = request.getSession();
                Long userId = (Long) session.getAttribute("userId");
                if(userId == null){
                    userId = userIdGenerator.getAndIncrement();
                }
                session.setAttribute("userId", userId);
                pageVariables.put("refreshPeriod", "1000");
                pageVariables.put("serverTime", getTime());
                pageVariables.put("userId", userId);
                response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
            }
            else{
                pageVariables.put("error", "Wrong password or username");
                response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            }
        }
        else
            if(request.getPathInfo().equals("/registration")) {
                user u = null;
                try{
                    u = Factory.getInstance().getUserDAO().getUserByName(login);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if (u == null){
                    u = new user();
                    u.setName(login);
                    u.setPass(pass);
                    try{
                        Factory.getInstance().getUserDAO().addUser(u);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    pageVariables.put("error", "Successfully registration");
                    response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
                }
                else {
                    pageVariables.put("error", "Already registarted");
                    response.getWriter().println(PageGenerator.getPage("registration.tml", pageVariables));
                }
            }
    }
}
