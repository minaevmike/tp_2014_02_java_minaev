package util;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;


public class HibernateUtil {
    private static SessionFactory sessionFactory = null;

    static {
        try {
            Configuration cfg = new Configuration().addResource("hibernate.cfg.xml");
            cfg.configure();
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
