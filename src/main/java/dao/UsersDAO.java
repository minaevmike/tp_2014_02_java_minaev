package dao;

import logic.User;

public interface UsersDAO {
    public void addUser(User user);
    public User getUserByName(String name);
    public void deleteUser(User user);
}
