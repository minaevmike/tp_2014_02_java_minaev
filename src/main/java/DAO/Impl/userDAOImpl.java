package dao.impl;

import dao.UsersDAO;
import org.hibernate.Session;
import util.HibernateUtil;

import logic.User;


public class UserDAOImpl implements UsersDAO {
    @Override
    public void addUser(User user) {
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
    public User getUserByName(String name){
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            user = (User) session.createQuery("from User where name = :name").setParameter("name", name).uniqueResult();
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
    public void deleteUser(User user){
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
