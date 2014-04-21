package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import util.resources.Database;
import util.resources.ResourceFactory;
import util.vfs.VFS;
import util.vfs.VFSImpl;

import java.io.File;


public class HibernateUtil {
    private static SessionFactory sessionFactory = null;

    static {
        try {
            VFS vfs = new VFSImpl("");
            Database database = (Database)ResourceFactory.getInstance().get(vfs.getAbsolutePath("data/db.xml"));
            Configuration cfg = new Configuration();
            cfg.configure(new File(vfs.getAbsolutePath(database.getConfig())));
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        }
        catch (JDBCConnectionException e){
            System.out.println("Database unreachable");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
