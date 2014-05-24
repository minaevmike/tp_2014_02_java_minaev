package messages;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AddressService {
    private Map<Class, List<Address>> services = new ConcurrentHashMap<>();
    private Map<Class, Iterator<Address>> iterators = new ConcurrentHashMap<>();
    private Address accountService;
    public Address getAccountService() {
        return accountService;
    }

    public void setAccountService(Address accountService) {
        this.accountService = accountService;
    }
    public void addService(Abonent abonent){
        if(services.get(abonent.getClass()) == null){
            services.put(abonent.getClass(), new CopyOnWriteArrayList<Address>());
            iterators.put(abonent.getClass(), services.get(abonent.getClass()).iterator());
        }
        services.get(abonent.getClass()).add(abonent.getAddress());
    }

    public Address getService(Class cl){
        Iterator<Address> iterator = iterators.get(cl);
        if(!iterator.hasNext()){
            iterator = services.get(cl).iterator();
        }

        return iterator.next();
    }
}
