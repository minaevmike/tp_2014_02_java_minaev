package messages;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import databaseservice.DatabaseService;
import logic.User;
import org.hibernate.exception.JDBCConnectionException;

public class MsgRegUser extends MsgToAS{
    private String login;
    private String password;
    private String sessionId;
    public MsgRegUser(Address from, Address to, String login,String password, String sessionId){
        super(from,to);
        this.login = login;
        this.password = password;
        this.sessionId = sessionId;
    }
    void exec(DatabaseService databaseService){
        User user = new User();
        user.setName(this.login);
        user.setPass(this.password);
        try{
            if(DatabaseService.addUser(user)){
                databaseService.getMessageSystem().sendMessage(new MsgRegStatus(getTo(),getFrom(),sessionId, "Successfully registrated"));
            }
            else {
                databaseService.getMessageSystem().sendMessage(new MsgRegStatus(getTo(),getFrom(),sessionId, "Duplicate username"));
            }
        }
        catch (JDBCConnectionException e){
            System.out.println("SADAD");
            databaseService.getMessageSystem().sendMessage(new MsgRegStatus(getTo(),getFrom(),sessionId, "Some works on server"));
        }
    }
}
