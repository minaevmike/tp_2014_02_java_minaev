package DAO;

import java.util.List;

import logic.User;

public interface usersDAO {
    public void addUser(User u);
    public User getUserByName(String n);
}
