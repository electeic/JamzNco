package jamilaappinc.grubmate;

/**
 * Created by melod on 10/14/2017.
 */

public class SubscriptionNotification extends Notification {
    public SubscriptionNotification(User mFromUser, Post mAboutPost, User mToUser){
        super(mFromUser,mAboutPost,mToUser);
    }
}
