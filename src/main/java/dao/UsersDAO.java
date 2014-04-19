package dao;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import logic.User;
import org.hibernate.HibernateException;
import org.hibernate.exception.JDBCConnectionException;

public interface UsersDAO {
    public void addUser(User user) throws HibernateException;
    public User getUserByName(String name) throws HibernateException;
    public void deleteUser(User user) throws HibernateException;
}
