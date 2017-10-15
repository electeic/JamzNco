package jamilaappinc.grubmate;

/**
 * Created by melod on 10/14/2017.
 */

public class RequestedNotification extends Notification {
    public RequestedNotification(User mFromUser, Post mAboutPost, User mToUser){
        super(mFromUser,mAboutPost,mToUser);
    }
}
