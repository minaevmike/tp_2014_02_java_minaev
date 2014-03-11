package DAO;

import logic.User;

public interface usersDAO {
    public void addUser(User user);
    public User getUserByName(String name);
    public void deleteUser(User user);
}
