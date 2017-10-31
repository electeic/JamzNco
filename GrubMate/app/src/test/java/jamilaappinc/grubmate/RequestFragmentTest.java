package jamilaappinc.grubmate;

import org.junit.Test;


import static org.junit.Assert.assertEquals;

/**
 * Created by ericajung on 10/30/17.
 */

public class RequestFragmentTest {

    @Test
    public void createRequestNoLocation() throws Exception {
        boolean requestCreated = RequestFragment.checkAllFilledAndWriteDBTest("", 1, "Erica", "Cookies");
        assertEquals(requestCreated,false);
    }

    @Test
    public void createRequestAllFilled() throws Exception {
        boolean requestCreated = RequestFragment.checkAllFilledAndWriteDBTest("SAL", 1, "Erica", "Cookies");
        assertEquals(requestCreated,true);
    }
}
