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
import messages.TimeHelper;
import templater.PageGenerator;
import logic.User;
import messages.*;

public class Frontend  extends HttpServlet implements Abonent,Runnable {
    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();
    private Map<String, String> sessionIdToStatus =  new HashMap<>();
    private static Object lock = new Object();
    final private static DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private static int handleCount = 0;
    Address address;
    MessageSystem ms;

    public Frontend(MessageSystem ms) {
        this.ms = ms;
        this.address = new Address();
        ms.addService(this);
    }

    public Address getAddress() {
        return address;
    }

    public static String getTime() {
        return formatter.format(new Date());
    }

    private static void timerVariables(HttpServletResponse response,String page,String userStatus) throws  IOException{
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("serverTime", getTime());
        pageVariables.put("userId",userStatus);
        response.getWriter().println(PageGenerator.getPage(page, pageVariables));
        //return pageVariables;
    }
    private static Map<String, Object> singleVaraiable(String name, String value){
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(name, value);
        return pageVariables;
    }
    public void setId(String sessionId, String userId) {
        UserSession userSession = sessionIdToUserSession.get(sessionId);
        if (userSession == null) {
            System.out.append("Can't find user session for: ").append(sessionId);
            return;
        }
        userSession.setUserId(userId);
    }

    public void setRegStatus(String sessionId, String status){
        sessionIdToStatus.put(sessionId, status);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        synchronized (lock){
            handleCount++;
        }
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        String sessionId = request.getSession().getId();
        switch (request.getPathInfo()){
            case "/registration":
                response.getWriter().println(PageGenerator.getPage("registration.tml", singleVaraiable("error", "")));
                break;
            case "/reging":{
                if (sessionIdToStatus.get(sessionId) == null){
                    response.getWriter().println(PageGenerator.getPage("reging.tml", singleVaraiable("refreshPeriod", "1000")));
                }
                else {
                    response.getWriter().println(PageGenerator.getPage("authform.tml", singleVaraiable("error", sessionIdToStatus.get(sessionId))));
                    sessionIdToStatus.remove(sessionId);
                }
                break;
            }
            case "/authform":
            default:
                UserSession userSession = sessionIdToUserSession.get(session.getId());
                if(userSession == null) {
                    timerVariables(response, "userId.tml", "Auth error");
                    return;
                }
                if(userSession.getUserId() == null) {
                    timerVariables(response,"userId.tml", "Wait for auth");
                    return;
                }
                timerVariables(response,"userId.tml", "name = " + userSession.getName() + ", id = " + userSession.getUserId());
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
        String sessionId = request.getSession().getId();
        switch (request.getPathInfo()){
            case "/authform": {
                UserSession userSession = new UserSession(sessionId, login, ms.getAddressService());
                sessionIdToUserSession.put(sessionId, userSession);
                Address frontendAddress = getAddress();
                Address accountServiceAddress = userSession.getAccountService();
                ms.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, pass, sessionId));
                timerVariables(response,"userId.tml","Auth Start");
                break;
            }
            case "/registration": {
                Address frontendAddress = getAddress();
                Address databaseServiceAddress = ms.getAddressService().getAccountService();
                ms.sendMessage(new MsgRegUser(frontendAddress,databaseServiceAddress,login,pass,sessionId));
                timerVariables(response,"reging.tml","Auth Start");
                response.sendRedirect("/reging");
                /*user = new User();
                user.setName(login);
                user.setPass(pass);
                if(DatabaseService.addUser(user)){
                    response.getWriter().println(PageGenerator.getPage("authform.tml", singleVaraiable("error", "Successfully registrated")));
                }
                else {
                    response.getWriter().println(PageGenerator.getPage("registration.tml", singleVaraiable("error", "Already registarted")));
                }*/
                break;
            }
            default:
                response.getWriter().println(PageGenerator.getPage("authform.tml",singleVaraiable("error", "Unknown rage")));

        }
    }

    public void run(){
        Long time = System.currentTimeMillis();
        while (true){
           ms.execForAbonent(this);
           TimeHelper.sleep(100);
        }
    }
}
