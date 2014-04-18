package dao;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import logic.User;
import org.hibernate.exception.JDBCConnectionException;

public interface UsersDAO {
    public void addUser(User user) throws JDBCConnectionException;
    public User getUserByName(String name);
    public void deleteUser(User user) throws JDBCConnectionException;
}
