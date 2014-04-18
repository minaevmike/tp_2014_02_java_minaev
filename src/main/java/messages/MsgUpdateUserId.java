package messages;

import frontend.Frontend;

public class MsgUpdateUserId extends MsgToFrontend {

    private String sessionId;
    private String id;

    public MsgUpdateUserId(Address from, Address to, String sessionId, String id) {
        super(from, to);
        this.sessionId = sessionId;
        this.id = id;
    }

    void exec(Frontend frontend) {
        frontend.setId(sessionId, id);
    }

}