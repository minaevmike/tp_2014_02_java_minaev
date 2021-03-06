

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import logic.User;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import databaseservice.DatabaseService;

public class DatabaseTest {
    @Test
    public void testGoodGetUser() throws CommunicationsException{
        User user = DatabaseService.getUserByName("test");
        Assert.assertNotNull(user);
    }

    @Test
    public void testFailGetUser() throws CommunicationsException {
        User user = DatabaseService.getUserByName(RandomStringUtils.randomAlphanumeric(50));
        Assert.assertNull(user);
    }

    @Test
    public void testGoodRegistration() throws CommunicationsException {
        User user = new User();
        user.setName(RandomStringUtils.randomAlphanumeric(50));
        user.setPass("test");
        Assert.assertTrue(DatabaseService.addUser(user));
    }

    @Test
    public void testFailRegistration() throws CommunicationsException {
        User user = new User();
        user.setName("test");
        user.setPass("test");
        Assert.assertFalse(DatabaseService.addUser(user));
    }

    @Test
    public void testGoodDeleteUser() throws CommunicationsException {
        User user = new User();
        user.setName(RandomStringUtils.randomAlphanumeric(50));
        user.setPass("123");
        DatabaseService.addUser(user);
        Assert.assertTrue(DatabaseService.deleteUser(user));
    }

    @Test
    public void testFailDeleteUser() throws CommunicationsException{
        User user = new User();
        user.setName(RandomStringUtils.randomAlphanumeric(50));
        user.setPass("123");
        Assert.assertFalse(DatabaseService.deleteUser(user));
    }
}
