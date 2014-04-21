package dao;

import logic.User;
import org.hibernate.HibernateException;

public interface UsersDAO {
    public void addUser(User user) throws HibernateException;
    public User getUserByName(String name) throws HibernateException;
    public void deleteUser(User user) throws HibernateException;
}
