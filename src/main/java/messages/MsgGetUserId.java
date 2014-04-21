package messages;

import databaseservice.DatabaseService;
import logic.User;
import org.hibernate.HibernateException;

public class MsgGetUserId extends MsgToAS {
    private String name;
    private String sessionId;
    private String password;

    public MsgGetUserId(Address from, Address to, String name, String password, String sessionId) {
        super(from, to);
        this.name= name;
        this.sessionId = sessionId;
        this.password = password;
    }

    void exec(DatabaseService databaseService) {
        User user;
        try{
            user = DatabaseService.getUserByName(this.name);
            if(user != null){
                if(user.getPass().equals(this.password)){
                    databaseService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),getFrom(),sessionId, user.getId().toString()));
                }
                else {
                    databaseService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),getFrom(),sessionId, "Wrong password"));
                }
            }
            else {
                databaseService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),getFrom(),sessionId, "No such user"));
            }
        }
        catch (HibernateException e){
            databaseService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),getFrom(),sessionId, "Some works on server"));
        }
    }
}