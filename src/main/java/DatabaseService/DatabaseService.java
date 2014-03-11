package DatabaseService;

import logic.User;
import DAO.Factory;


public class DatabaseService {
    public static User getUserByName(String name) {
        return Factory.getInstance().getUserDAO().getUserByName(name);
    }

    public static boolean addUser(User user){
        if(getUserByName(user.getName()) == null){
            Factory.getInstance().getUserDAO().addUser(user);
            return true;
        }
        else
            return false;
    }

    public static boolean deleteUser(User user){
        if(getUserByName(user.getName()) != null){
            Factory.getInstance().getUserDAO().deleteUser(user);
            return true;
        }
        else
            return false;
    }

}
