package messages;


import frontend.Frontend;

public class MsgRegStatus extends MsgToFrontend {
    private String status;
    private String sessionId;

    public MsgRegStatus(Address from, Address to, String sessionId, String status){
        super(from, to);
        this.status = status;
        this.sessionId = sessionId;
    }

    void exec(Frontend frontend){
        frontend.setRegStatus(sessionId, status);
    }
}
