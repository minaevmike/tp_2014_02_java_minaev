package messages;

import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageSystem {
    private Map<Address, ConcurrentLinkedQueue<Msg>> messages = new HashMap<>();
    private AddressService addressService = new AddressService();

    public void addService(Abonent abonent){
        addressService.addService(abonent);
        messages.put(abonent.getAddress(), new ConcurrentLinkedQueue<Msg>());
    }

    public void sendMessage(Msg message){
        Queue<Msg> messageQueue = messages.get(message.getTo());
        messageQueue.add(message);

    }

    public void execForAbonent(Abonent abonent) {
        Queue<Msg> messageQueue = messages.get(abonent.getAddress());
        if(messageQueue == null){
            return;
        }
        while(!messageQueue.isEmpty()){
            Msg message = messageQueue.poll();
            message.exec(abonent);
        }
    }

    public Map<Address, ConcurrentLinkedQueue<Msg>> getMessages() {
        return messages;
    }

    public AddressService getAddressService(){
        return addressService;
    }

    public void registerService(Abonent abonent){

    }
    @TestOnly
    public  boolean isQueueEmpty(Abonent abonent){
        return messages.get(abonent.getAddress()).isEmpty();
    }
}
