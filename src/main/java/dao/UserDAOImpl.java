package dao;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import dao.UsersDAO;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.UnknownServiceException;
import util.HibernateUtil;

import logic.User;


public class UserDAOImpl implements UsersDAO {
    @Override
    public void addUser(User user) throws JDBCConnectionException{
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    @Override
    public User getUserByName(String name) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (User) session.createQuery("from User where name = :name").setParameter("name", name).uniqueResult();
        }
        catch (JDBCConnectionException e){
            System.out.println("Catch");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return user;
    }

    @Override
    public void deleteUser(User user) throws JDBCConnectionException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
    }
}
