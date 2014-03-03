package DAO.Impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import util.HibernateUtil;

import DAO.usersDAO;
import logic.user;


public class userDAOImpl implements usersDAO {
    @Override
    public void addUser(user u) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(u);
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
    public void updateUser(user u) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(u);
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
    public user getUserById(Long id) throws SQLException {
        Session session = null;
        user u = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            u = (user) session.load(user.class, id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return u;
    }

    @Override
    public List<user> getAllUsers() throws SQLException {
        Session session = null;
        List<user> users = new ArrayList<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            users = session.createCriteria(user.class).list();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return users;
    }

    @Override
    public void deleteUser(user u) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(u);
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

    @Override
    public user getUserByName(String n) throws SQLException{
        Session session = null;
        user u = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            u = (user) session.createQuery("from user where name = :name").setParameter("name", n).uniqueResult();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if (session != null && session.isOpen()){
                session.close();
            }
        }
        return u;
    }
}
