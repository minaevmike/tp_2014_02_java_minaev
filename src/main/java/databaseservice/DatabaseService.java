package databaseservice;

import logic.User;
import dao.Factory;
import messages.Abonent;
import messages.Address;
import messages.MessageSystem;
import messages.TimeHelper;
import org.hibernate.HibernateException;
import org.hibernate.exception.JDBCConnectionException;


public class DatabaseService implements Abonent,Runnable {
    private Address address;
    private MessageSystem ms;

    public DatabaseService(MessageSystem ms){
        this.ms = ms;
        this.address = new Address();
        ms.addService(this);
        ms.getAddressService().setAccountService(address);
    }

    public static User getUserByName(String name) throws HibernateException{
        TimeHelper.sleep(2000);
        return Factory.getInstance().getUserDAO().getUserByName(name);
    }

    public static boolean addUser(User user) throws HibernateException {
        TimeHelper.sleep(2000);
        try{
            if(getUserByName(user.getName()) == null){
                Factory.getInstance().getUserDAO().addUser(user);
                return true;
            }
            else
                return false;
            }
        catch (HibernateException e){
            e.printStackTrace();
            throw e;
        }
    }
    public void run(){
        while(true){
            ms.execForAbonent(this);
            TimeHelper.sleep(10);
        }
    }
    public static boolean deleteUser(User user) throws JDBCConnectionException {
        if(getUserByName(user.getName()) != null){
            Factory.getInstance().getUserDAO().deleteUser(user);
            return true;
        }
        else
            return false;
    }
    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem(){
        return ms;
    }

}
