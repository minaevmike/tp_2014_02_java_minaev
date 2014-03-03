package DAO;

import java.sql.SQLException;
import java.util.List;

import logic.user;

public interface usersDAO {
    public void addUser(user u) throws SQLException;
    public void updateUser(user u) throws SQLException;
    public user getUserById(Long id) throws SQLException;
    public List getAllUsers() throws SQLException;
    public void deleteUser(user u) throws SQLException;
    public user getUserByName(String n) throws SQLException;
}
