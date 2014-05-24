package frontend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import databaseservice.DatabaseService;
import messages.TimeHelper;
import templater.PageGenerator;
import messages.*;
import util.resources.ResourceFactory;
import util.resources.Templates;
import util.vfs.VFS;
import util.vfs.VFSImpl;

public class Frontend  extends HttpServlet implements Abonent,Runnable {
    private Map<String, UserSession> sessionIdToUserSession = new HashMap<>();
    private Map<String, String> sessionIdToStatus =  new HashMap<>();
    final private static DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    final private static VFS vfs = new VFSImpl("data" + File.separator);
    private static Templates templates = (Templates)ResourceFactory.getInstance().get(vfs.getAbsolutePath("templates.xml"));
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
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        String sessionId = request.getSession().getId();
        switch (request.getPathInfo()){
            case "/registration":
                response.getWriter().println(PageGenerator.getPage(templates.getRegistration(), singleVaraiable("error", "")));
                break;
            case "/reging":{
                if (sessionIdToStatus.get(sessionId) == null){
                    response.getWriter().println(PageGenerator.getPage(templates.getReging(), singleVaraiable("refreshPeriod", "1000")));
                }
                else {
                    response.getWriter().println(PageGenerator.getPage(templates.getAuth(), singleVaraiable("error", sessionIdToStatus.get(sessionId))));
                    sessionIdToStatus.remove(sessionId);
                }
                break;
            }
            case "/authform":
                UserSession userSession = sessionIdToUserSession.get(session.getId());
                if(userSession == null) {
                    timerVariables(response, templates.getUserId(), "Auth error");
                    return;
                }
                if(userSession.getUserId() == null) {
                    timerVariables(response, templates.getUserId(), "Wait for auth");
                    return;
                }
                timerVariables(response,templates.getUserId(), "You login name = " + userSession.getName() + ", id = " + userSession.getUserId());
                break;
            default:
                response.getWriter().println(PageGenerator.getPage(templates.getAuth(), singleVaraiable("error", "")));
        }
    }

    public void setMs(MessageSystem ms) {
        this.ms = ms;
        ms.addService(this);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
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
                timerVariables(response,templates.getUserId(),"Auth Start");
                break;
            }
            case "/registration": {
                Address frontendAddress = getAddress();
                Address databaseServiceAddress = ms.getAddressService().getService(DatabaseService.class);
                ms.sendMessage(new MsgRegUser(frontendAddress,databaseServiceAddress,login,pass,sessionId));
                timerVariables(response, templates.getReging(), "Reg Start");
                response.sendRedirect("/reging");
                break;
            }
            default:
                response.getWriter().println(PageGenerator.getPage(templates.getAuth(),singleVaraiable("error", "Unknown rage")));

        }
    }

    public void run(){
        while (true){
           ms.execForAbonent(this);
           TimeHelper.sleep(100);
        }
    }
}
