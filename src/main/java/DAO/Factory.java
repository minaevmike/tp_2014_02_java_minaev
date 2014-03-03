package DAO;

import DAO.Impl.userDAOImpl;

public class Factory {

    private static usersDAO userDAO = null;
    private static Factory instance = null;

    public static synchronized Factory getInstance(){
        if (instance == null){
            instance = new Factory();
        }
        return instance;
    }

    public usersDAO getUserDAO(){
        if(userDAO == null){
            userDAO = new userDAOImpl();
        }
        return userDAO;
    }
}
