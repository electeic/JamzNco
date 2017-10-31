package jamilaappinc.grubmate;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Terence Zhang on 10/30/2017.
 */
public class PostFragmentTest {
    @Test
    public void createPostNoTitle() throws Exception {
        boolean postCreated = PostFragment.checkAllFilledAndWriteDBTest("","Milk", "SAL", "3", "honey,cake", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(postCreated,false);
    }
    @Test
    public void createPostNoLocation() throws Exception {
        boolean postCreated = PostFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "", "3", "honey,cake", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(postCreated,false);
    }
    @Test
    public void createPostNoServings() throws Exception {
        boolean postCreated = PostFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "", "honey,cake", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(postCreated,false);
    }
    @Test
    public void createPostNoTags() throws Exception {
        boolean postCreated = PostFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(postCreated,false);
    }
    @Test
    public void createPostNoSDate() throws Exception {
        boolean postCreated = PostFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "honey,cake", null, new Date(2017,10,31,10,8));
        assertEquals(postCreated,false);
    }
    @Test
    public void createPostNoEDate() throws Exception {
        boolean postCreated = PostFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "honey,cake", new Date(2017,10,30,10,8), null);
        assertEquals(postCreated,false);
    }
    @Test
    public void createPostAllCorrect() throws Exception {
        boolean postCreated = PostFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "honey,cake", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(postCreated,true);
    }
}