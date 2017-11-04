package jamilaappinc.grubmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
// *
 * to handle interaction events.
 * Use the {@link ViewNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewNotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARGS_POSITION = "args_position";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    android.support.design.widget.FloatingActionButton floatButton;
    ListView list;
    NotifAdapter adapter;
    private String ID;
    FirebaseDatabase database;
    DatabaseReference dbRefUsers;
    DatabaseReference dbRefNotifications;
    DatabaseReference dbRef, dbRefRequests;



    ArrayList<Notification> notifications = new ArrayList<>();

    ArrayList<Integer> notifReadCounter = new ArrayList<>();
    ArrayList<Integer> notifCount = new ArrayList<>();
    ArrayList<String> userFriends;
    String currUserName;


    public ViewNotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewNotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters

//            LINE FOR GETTING ALL THE NOTIFICATIONS :
    public static ViewNotificationsFragment newInstance(ArrayList<Notification> notifications){
        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, pos);
        ViewNotificationsFragment fragment = new ViewNotificationsFragment();
//            LINE FOR GETTING ALL THE NOTIFICATIONS :
        args.putSerializable(ViewNotificationsActivity.GET_ALL_NOTIFICATIONS, notifications);

        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_notifications, container, false);
        initComponents(v);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        userFriends = (ArrayList<String>) i.getSerializableExtra("Users");
        currUserName = i.getStringExtra("Name");
        //Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
//        populateList();
        addListeners();




        return v;
    }

    private void initComponents(View v){
        database = FirebaseDatabase.getInstance();
        dbRef = database.getInstance().getReference();
        dbRefUsers = database.getInstance().getReference().child(FirebaseReferences.USERS);
        dbRefNotifications = database.getInstance().getReference().child(FirebaseReferences.NOTIFICATIONS);
        dbRefRequests = database.getInstance().getReference().child(FirebaseReferences.REQUEST);

        list = (ListView) v.findViewById(R.id.notifications_list);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        notifReadCounter.add(0);
//            LINE FOR GETTING ALL NOTIFICATIONS
//        notifications = (ArrayList<Notification>)getArguments().getSerializable(ViewNotificationsActivity.GET_ALL_NOTIFICATIONS);
    }


    private void addListeners(){
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) { //goes through posts, find the number of posts
                    if (snap.getKey().equals("Notification")) { //if it
                        notifCount.add((int)snap.getChildrenCount());
//                        System.out.println("ADDED # NOTIFICATION, count is " + snap.getChildrenCount());
                    }
                }
                dbRefNotifications.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    int postsRead = notifReadCounter.get(0);
                    postsRead++;
                    notifReadCounter.clear();
                    notifReadCounter.add(postsRead);
                    Notification notif = dataSnapshot.getValue(Notification.class);
                    if(notif.getmToUser().equals(ID)){
                        notifications.add(notif);
                    }

                    if(notifReadCounter.get(0) == notifCount.get(0)) {
                        adapter = new NotifAdapter(getActivity(), notifications);
                        list.setAdapter(adapter);
                    }



                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
        });

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("Name",currUserName);
                intent.putExtra("Users", userFriends);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Notification notification = (Notification) list.getItemAtPosition(position);

                if (notification.getmType().equals(NotificationReference.REQUEST)) {
                    /*if notification is a request notification then go to the request page
                        to show the request information
                    */
                    dbRefNotifications.child(notification.getmId()).setValue(null);
                    dbRefUsers.child(ID).child(FirebaseReferences.MYNOTIFICATIONS).child(notification.getmId()).setValue(null);
//                    dbRefRequests.child(notification.getmId()).setValue(null);
                    notifications.remove(position);
                    adapter.notifyDataSetChanged();


                    dbRefRequests.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snap: dataSnapshot.getChildren()){
                                Request request = snap.getValue(Request.class);
                                if(request.getmId().equals(notification.getmId())){
                                    Intent i = new Intent(getActivity(), ViewRequestNotificationActivity.class);
                                    i.putExtra("Request", request);
                                    i.putExtra("ID", notification.getmToUser());
                                    i.putExtra("Users", userFriends);
                                    i.putExtra("Name",currUserName);
                                    i.putExtra(ViewRequestNotificationActivity.GET_REQUEST,notification);
                                    i.putExtra(ViewNotificationsActivity.GET_ALL_NOTIFICATIONS, notifications);
                                    startActivity(i);
                                }


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }else if (notification.getmType().equals(NotificationReference.RATE)){
                    dbRefNotifications.child(notification.getmId()).setValue(null);
                    dbRefUsers.child(ID).child(FirebaseReferences.MYNOTIFICATIONS).child(notification.getmId()).setValue(null);
                    notifications.remove(position);
                    adapter.notifyDataSetChanged();
                    Intent i = new Intent(getActivity(), RateUserActivity.class);
                    i.putExtra("ID", ID);
                    i.putExtra("Users", userFriends);
                    i.putExtra("Name",currUserName);
                    i.putExtra("Notification", notification);
           /*         i.putExtra(RateUserActivity.GET_RATER_USER,notification.getmToUser());
                    i.putExtra(RateUserActivity.GET_RATE_REQUEST,notification);
                    i.putExtra(ViewNotificationsActivity.GET_ALL_NOTIFICATIONS, notifications);*/
                    startActivity(i);
                }
                else{
                    /* tell database to delete/deactivate the notification & delete from screen */
                    dbRefNotifications.child(notification.getmId()).setValue(null);
                    dbRefUsers.child(ID).child(FirebaseReferences.MYNOTIFICATIONS).child(notification.getmId()).setValue(null);
                    notifications.remove(position);
                    adapter.notifyDataSetChanged();
                   // Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO DELETE NOTIFICATION FROM DB" , Toast.LENGTH_SHORT).show();

                    Log.d("Damn", "it didn't recognize the type");
                }

            }
        });


    }




    private class NotifAdapter extends ArrayAdapter<Notification> {
        List<Notification> notifications;
        public NotifAdapter(Context context, List<Notification> notifications){
            super(context,0,notifications );
            this.notifications = notifications;
        }

        public View getView(int position, View itemView, ViewGroup parent){
            //find the notification to work with
            Notification notif = notifications.get(position);

//            Post relevantPost = notif.getmAboutPost();
//            User fromUser = notif.getmFromUser();

            if(notif != null) {
                if (notif.getmToUser().equals(ID)) {
                    if (notif.getmType().equals(NotificationReference.SUBSCRIPTION)) {
                        itemView = LayoutInflater.from(getContext()).inflate(R.layout.notification_info_subscription, parent, false);
                        TextView title = (TextView) itemView.findViewById(R.id.notification_info_subscription_title);
                        title.setText(notif.getmTitle() + " has matched a subscription!");

                    } else if (notif.getmType().equals(NotificationReference.REQUEST)) {
                        itemView = LayoutInflater.from(getContext()).inflate(R.layout.notification_info_request, parent, false);
                        TextView title = (TextView) itemView.findViewById(R.id.notification_info_request_title);
//                        System.out.println("meldoy help " + notif.getmFromUser() + " " + notif.get)
                         title.setText(notif.getmFromUserName() + " has made a request about " + notif.getmTitle());


                    } else if (notif.getmType().equals(NotificationReference.RATE)) {
                        itemView = LayoutInflater.from(getContext()).inflate(R.layout.notification_info_rate_user, parent, false);
                        TextView title = (TextView) itemView.findViewById(R.id.notification_info_rate_title);
                        title.setText("Please rate " + notif.getmFromUserName() + ".");

                    } else {
                        itemView = LayoutInflater.from(getContext()).inflate(R.layout.notification_info_accept, parent, false);
                        TextView title = (TextView) itemView.findViewById(R.id.notification_info_accept_title);
                        title.setText(notif.getmFromUser() + " has accepted your request regarding " + notif.getmTitle() + ".");

                    }

                }
            }
            return itemView;

// return  super.getView(position convertView, parent)
        }

    }


    /*
    private void populateList() {
        //NOTE: I created another constructer in Post so that testing would be easier
        notifications.add(new SubscriptionNotification(new User("Abby Mcpherson", "pic"),new Post("Bless my soul" ,1), new User("Jacob Badillo", "pic2")  ));
        notifications.add(new RequestedNotification(new User("Chipotle Cha", "pic"),new Post("Eating food",3), new User("Jacob Badillo", "pic2")  ));
        notifications.add(new AcceptedNotification(new User("Linda Belcher", "pic"),new Post("Posty post post",4), new User("Jacob Badillo", "pic2")  ));
        notifications.add(new RateNotification(new User("Tina Fey", "pic"),new Post("Hamburgers",3), new User("Jacob Badillo", "pic2")  ));
//        notifications.add(new SubscriptionNotification(new User("Abby Mcpherson", "pic"),new Post("Title of post"), new User("Jacob Badillo", "pic2")  ));
//        notifications.add(new RequestedNotification(new User("Abby Mcpherson", "pic"),new Post("Title of post"), new User("Jacob Badillo", "pic2")  ));
//        notifications.add(new AcceptedNotification(new User("Abby Mcpherson", "pic"),new Post("Title of post"), new User("Jacob Badillo", "pic2")  ));
//        notifications.add(new SubscriptionNotification(new User("Abby Mcpherson", "pic"),new Post("Title of post"), new User("Jacob Badillo", "pic2")  ));
//        notifications.add(new RequestedNotification(new User("Abby Mcpherson", "pic"),new Post("Title of post"), new User("Jacob Badillo", "pic2")  ));
//        notifications.add(new AcceptedNotification(new User("Abby Mcpherson", "pic"),new Post("Title of post"), new User("Jacob Badillo", "pic2")  ));
//        notifications.add(new RateNotification(new User("Abby Mcpherson", "pic"),new Post("Title of post"), new User("Jacob Badillo", "pic2")  ));

    }


     */




/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

//    @Override
/*    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

/*    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
