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

import databaseservice.DatabaseService;
import templater.PageGenerator;
import logic.User;

public class Frontend  extends HttpServlet implements Runnable {

    private static Object lock = new Object();
    final private static DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private static int handleCount = 0;
    public static String getTime() {
        return formatter.format(new Date());
    }

    private static Map<String, Object> timerVariables(Long userId){
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        pageVariables.put("userId",userId);
        return pageVariables;
    }
    private static Map<String, Object> singleVaraiable(String name, String value){
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(name, value);
        return pageVariables;
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        synchronized (lock){
            handleCount++;
        }
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        Map<String, Object> pageVariables;
        switch (request.getPathInfo()){
            case "/registration":
                pageVariables = singleVaraiable("error", "");
                response.getWriter().println(PageGenerator.getPage("registration.tml", pageVariables));
                break;
            case "/authform":
            default:
                Long userId = (Long) session.getAttribute("userId");
                if(userId != null) {
                    pageVariables = timerVariables(userId);
                    response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
                }
                else {
                    pageVariables = singleVaraiable("error", "");
                    response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
                }
                break;

        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        synchronized (lock){
            handleCount++;
        }
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables;
        User user;
        switch (request.getPathInfo()){
            case "/authform":
                user = DatabaseService.getUserByName(login);
                if(user != null && user.getPass().equals(pass)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", user.getId());
                    pageVariables = timerVariables(user.getId());
                    response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
                }
                else{
                    pageVariables = singleVaraiable("error", "Wrong password or username");
                    response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
                }
                break;
            case "/registration":
                user = new User();
                user.setName(login);
                user.setPass(pass);
                if(DatabaseService.addUser(user)){
                    pageVariables = singleVaraiable("error", "Successfully registrated");
                    response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
                }
                else {
                    pageVariables = singleVaraiable("error", "Already registarted");
                    response.getWriter().println(PageGenerator.getPage("registration.tml", pageVariables));
                }
                break;
            default:
                pageVariables = singleVaraiable("error", "Unknown rage");
                response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));

        }
    }

    public void run(){
        Long time = System.currentTimeMillis();
        while (true){
            if(System.currentTimeMillis() - time > 5000){
                System.out.println(handleCount);
                time = System.currentTimeMillis();
            }
        }
    }
}
