package jamilaappinc.grubmate;

/**
 * Created by melod on 10/14/2017.
 */

public class AcceptedNotification extends Notification {
    public AcceptedNotification(User mFromUser, Post mAboutPost, User mToUser){
        super(mFromUser,mAboutPost,mToUser);
    }
}