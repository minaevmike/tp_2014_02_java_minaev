package messages;

import databaseservice.DatabaseService;

public abstract class MsgToAS extends Msg{

    public MsgToAS(Address from, Address to) {
        super(from, to);
    }

    void exec(Abonent abonent) {
        if(abonent instanceof DatabaseService){
            exec((DatabaseService) abonent);
        }
    }

    abstract void exec(DatabaseService databaseService);
}