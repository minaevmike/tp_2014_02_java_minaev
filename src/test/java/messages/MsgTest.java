package messages;

import frontend.Frontend;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sun.management.resources.agent_de;

/**
 * Created by Mike on 20.04.14.
 */
public class MsgTest {
    private class testMsg extends Msg{
        private boolean wasExecuted = false;

        public testMsg(Address from, Address to) {
            super(from, to);
        }

        public void exec(Abonent abonent) {
            wasExecuted = true;
        }
    }
    private static final Msg msg = Mockito.mock(Msg.class);
    private  static MessageSystem ms = new MessageSystem();
    private static Frontend frontend = Mockito.mock(Frontend.class);
    private static Frontend frontendTwo = Mockito.mock(Frontend.class);
    @Before
    public void setUp() throws Exception {
        Address address = new Address();
        Address anotherAdress = new Address();
        Mockito.when(msg.getTo()).thenReturn(address);
        Mockito.when(frontend.getAddress()).thenReturn(address);
        Mockito.when(frontendTwo.getAddress()).thenReturn(anotherAdress);
    }

    @Test
    public void testAddGood(){
        ms.addService(frontend);
        Assert.assertTrue(ms.getMessages().get(frontend.getAddress()) != null);
    }
    @Test
    public void testSendMsg(){
        ms.addService(frontend);
        ms.sendMessage(msg);
        Assert.assertTrue(ms.getMessages().get(frontend.getAddress()) != null);
    }

    @Test
    public void testExec(){
        ms.addService(frontend);
        ms.addService(frontendTwo);
        testMsg msgTest = new testMsg(frontend.getAddress(), frontendTwo.getAddress());
        ms.sendMessage(msgTest);
        ms.execForAbonent(frontendTwo);
        Assert.assertTrue(msgTest.wasExecuted);
    }
}
