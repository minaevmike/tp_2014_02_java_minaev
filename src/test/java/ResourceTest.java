import org.junit.Assert;
import util.resources.ResourceFactory;
import util.resources.Test;
import util.vfs.VFS;
import util.vfs.VFSImpl;

public class ResourceTest {
    private String file = "data/test.xml";

    @org.junit.Test
    public void testGetObject(){
        VFS vfs = new VFSImpl("");
        Test a = (Test) ResourceFactory.getInstance().get(vfs.getAbsolutePath(file));
        Assert.assertTrue(a != null);
    }
    @org.junit.Test
    public void testGetObjectBody(){
        VFS vfs = new VFSImpl("");
        Test a = (Test) ResourceFactory.getInstance().get(vfs.getAbsolutePath(file));
        Assert.assertTrue(a.getA() == 1);
        Assert.assertTrue(a.getB().equals("abccba"));
    }
}
