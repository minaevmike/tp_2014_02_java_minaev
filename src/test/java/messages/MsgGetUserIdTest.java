package messages;

import databaseservice.DatabaseService;
import frontend.Frontend;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class MsgGetUserIdTest {
    private static Address address = new Address();
    private static MessageSystem ms = new MessageSystem();
    private static Frontend frontend = Mockito.mock(Frontend.class);
    private static DatabaseService databaseService = new DatabaseService(ms);
    private static String login = RandomStringUtils.randomAlphanumeric(10);
    private static String pass = RandomStringUtils.randomAlphanumeric(10);
    private static String sessionId = RandomStringUtils.randomAlphanumeric(10);
    //private static Frontend frontend = new Frontend(ms);
    @Before
    public void setUp() throws Exception {
        (new Thread(frontend)).start();
        (new Thread(databaseService)).start();
        Mockito.when(frontend.getAddress()).thenReturn(address);
        frontend.setMs(ms);
        ms.addService(frontend);
    }

    private void waitEmpty(Abonent abonent){
        while(ms.isQueueEmpty(abonent)){
            TimeHelper.sleep(100);
        }
    }
    @Test
     public void testAuthMsg(){
        UserSession userSession = new UserSession(sessionId, login, ms.getAddressService());
        Address frontendAddress = frontend.getAddress();
        Address accountServiceAddress = userSession.getAccountService();
        ms.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, pass, sessionId));
        waitEmpty(frontend);
        ms.execForAbonent(frontend);
        Mockito.verify(frontend, Mockito.atLeastOnce()).setId(Mockito.anyString(),Mockito.anyString());
    }
    @Test
    public void testRegMsg(){
        Address frontendAddress = frontend.getAddress();
        ms.sendMessage(new MsgRegUser(frontendAddress,ms.getAddressService().getAccountService(),login,pass,sessionId));
        waitEmpty(frontend);
        ms.execForAbonent(frontend);
        Mockito.verify(frontend, Mockito.atLeastOnce()).setRegStatus(Mockito.anyString(),Mockito.anyString());
    }
}
