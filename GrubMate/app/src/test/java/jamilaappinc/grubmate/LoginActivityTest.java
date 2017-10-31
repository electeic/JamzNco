package jamilaappinc.grubmate;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Terence Zhang on 10/30/2017.
 */
public class LoginActivityTest {
    @Test
    public void testUserExistsWithExistingUser() throws Exception {
        boolean userExists = LoginActivity.checkUserExistsInDB("Ivan Chen");
        assertEquals(userExists,true);
    }
    @Test
    public void testUserExistsWithNonExistentUser() throws Exception {
        boolean userExists = LoginActivity.checkUserDoesNotExistsInDB("Myrl Mar");
        assertEquals(userExists,true);
    }
    @Test
    public void testUserFriendsWithActualFriend() throws Exception {
        boolean friendExists = LoginActivity.checkUserFriendsWithActualFriend("Jamila Abu-Omar","10203748463708010");
        assertEquals(friendExists,true);
    }
    @Test
    public void testUserFriendsWithIncorrectFriend() throws Exception {
        boolean friendExists = LoginActivity.checkUserFriendsWithIncorrectFriend("Jamila Abu-Omar","10210716973863610");
        assertEquals(friendExists,true);
    }


}