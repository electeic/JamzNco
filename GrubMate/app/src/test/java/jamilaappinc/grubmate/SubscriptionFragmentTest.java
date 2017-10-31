package jamilaappinc.grubmate;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by ericajung on 10/30/17.
 */

public class SubscriptionFragmentTest {
    @Test
    public void createSubNothingFilled() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("","", "", "", "", "", null, null);
        assertEquals(subCreated,false);
    }
    @Test
    public void createSubNoLocation() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "", "3", "honey,cake", "chinese,american", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(subCreated,false);
    }
    @Test
    public void createSubNoServings() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "", "honey,cake", "chinese,american", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(subCreated,false);
    }
    @Test
    public void createSubNoTags() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "", "chinese,american", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(subCreated,false);
    }
    @Test
    public void createSubNoCats() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "honey,cake", "", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(subCreated,false);
    }
    @Test
    public void createSubNoSDate() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "honey,cake", "chinese,american", null, new Date(2017,10,31,10,8));
        assertEquals(subCreated,false);
    }
    @Test
    public void createSubNoEDate() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "honey,cake", "chinese,american", new Date(2017,10,30,10,8), null);
        assertEquals(subCreated,false);
    }
    @Test
    public void createSubEDateBeforeSDate() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "honey,cake", "chinese,american", new Date(2017,10,30,10,8), new Date(2017,9,30,10,8));
        assertEquals(subCreated,true);
    }
    @Test
    public void createSubAllCorrect() throws Exception {
        boolean subCreated = createsSubscriptionFragment.checkAllFilledAndWriteDBTest("Honey Cake","Milk", "SAL", "3", "honey,cake", "chinese,american", new Date(2017,10,30,10,8), new Date(2017,10,31,10,8));
        assertEquals(subCreated,true);
    }
}
