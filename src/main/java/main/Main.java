package main;
import databaseservice.DatabaseService;
import frontend.Frontend;
import messages.MessageSystem;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import util.HibernateUtil;
import util.resources.ResourceFactory;
import util.vfs.VFS;
import util.vfs.VFSImpl;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws Exception {
        VFS vfs = new VFSImpl("");
        util.resources.Connection con = (util.resources.Connection)ResourceFactory.getInstance().get(vfs.getAbsolutePath("data/server.xml"));
        Iterator<String> files = vfs.getIterator("data");
        while (files.hasNext()){
            String nextFile = files.next();
            if (!vfs.isDirectory(nextFile)){
                System.out.println(vfs.getAbsolutePath(nextFile));
                ResourceFactory.getInstance().add(vfs.getAbsolutePath(nextFile));
            }
        }
        MessageSystem ms = new MessageSystem();
        Frontend frontend = new Frontend(ms);
        DatabaseService databaseService = new DatabaseService(ms);
        (new Thread(frontend)).start();
        (new Thread(databaseService)).start();
        (new Thread(databaseService)).start();
        HibernateUtil.getSessionFactory();
        Server server = new Server(con.getPort());
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        context.addServlet(new ServletHolder(frontend), "/*");
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
