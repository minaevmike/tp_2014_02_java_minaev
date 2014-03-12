package dao;

import dao.impl.UserDAOImpl;

public class Factory {

    private static UsersDAO userDAO = null;
    private static Factory instance = null;

    public static synchronized Factory getInstance(){
        if (instance == null){
            instance = new Factory();
        }
        return instance;
    }

    public UsersDAO getUserDAO(){
        if(userDAO == null){
            userDAO = new UserDAOImpl();
        }
        return userDAO;
    }
}
