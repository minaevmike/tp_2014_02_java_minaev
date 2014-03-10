package DatabaseService;

import logic.User;
import DAO.Factory;


public class DatabaseService {
    public static User getUserByName(String name) {
        User user = Factory.getInstance().getUserDAO().getUserByName(name);
        return user;
    }
    public static boolean addUser(User user){
        if(getUserByName(user.getName()) == null){
            Factory.getInstance().getUserDAO().addUser(user);
            return true;
        }
        else
            return false;
    }

}
