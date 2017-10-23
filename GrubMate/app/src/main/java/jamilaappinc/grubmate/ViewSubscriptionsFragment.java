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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ViewSubscriptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewSubscriptionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";
    ListView list;
    SubscriptionAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    android.support.design.widget.FloatingActionButton floatButton;
    private String ID;
    FirebaseDatabase database;
    DatabaseReference dbRefSubs;

    ArrayList<Integer> subsReadCounter = new ArrayList<>();
    ArrayList<Integer> subsCount = new ArrayList<>();

    /*
        THIS IS FOR DYNAMIC MAYBE NOT REALLY SURE
        private ArrayList<Subscription> subscriptions;

        IF IT IS THEN DELETE THE BOTTOM ONE
     */
    private ArrayList<Subscription> subscript = new ArrayList<Subscription>();


//    private OnFragmentInteractionListener mListener;

    public ViewSubscriptionsFragment() {
        // Required empty public constructor
    }

//        THIS IS FOR DYNAMIC
     public static ViewSubscriptionsFragment newInstance(ArrayList<Subscription> subscriptions){
        ViewSubscriptionsFragment fragment = new ViewSubscriptionsFragment();
        Bundle args = new Bundle();
//            THIS IS FOR DYNAMIC
        args.putSerializable(ViewSubscriptionsActivity.GET_ALL_SUBSCRIPTIONS, subscriptions);
//        args.putInt(ViewSubscriptionsActivity.GET_SUBSCRIPTION, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_subscriptions, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        //Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
//        populateList();
        initComponents(v);
        addListeners();
        database = FirebaseDatabase.getInstance();
        dbRefSubs = database.getInstance().getReference().child(FirebaseReferences.MYSUBS);
        subsReadCounter.add(0);

        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) { //goes through posts, find the number of posts
                    Log.e(snap.getKey() + " GETTING NUM KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Subscription")) { //if it
                        subsCount.add((int)snap.getChildrenCount());
                        System.out.println("ADDED # SUBS, count is " + snap.getChildrenCount());
                    }
                }
                dbRefSubs.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        int postsRead = subsReadCounter.get(0);
                        postsRead++;
                        System.out.println("POSTS READ COUNT " + postsRead);
                        subsReadCounter.clear();
                        subsReadCounter.add(postsRead);


                        Subscription post = dataSnapshot.getValue(Subscription.class);

                        if(post.getmUserAuthorId().equals(ID)){
                            subscript.add(post);
                            System.out.println("I GOT A POST!!" + post);
                        }

                        if(subsReadCounter.get(0) == subsCount.get(0)){
                            System.out.println("IN IF, SETTING ADAPTER NOW");
                            adapter = new SubscriptionAdapter(getActivity(), R.layout.list_active_posts_item, subscript);
                            //        postList = PostSingleton.get(getActivity()).getMovies();
                            //        postAdapter = new PostListAdapter(getActivity(), Post.class,
                            //                R.layout.list_active_posts_item,
                            //                dbRefPosts);
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


        return v;
    }
    private void initComponents(View v){
//        subscript = (ArrayList<String>)getArguments().getSerializable(ViewSubscriptionsActivity.GET_ALL_SUBSCRIPTIONS);
//        adapter = new SubscriptionAdapter(getActivity());
        list = (ListView) v.findViewById(R.id.viewSubscriptions_list);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
    }

    private void addListeners(){
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subscription subscription = (Subscription) list.getItemAtPosition(position);
                subscript.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO DELETE SUBSCRIPTION FROM DB" , Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class SubscriptionAdapter extends ArrayAdapter<Subscription> {
        List<Subscription> subs;
        Context context;

        public SubscriptionAdapter(Context context, int resource, List<Subscription> objects) {
            super(context, resource, objects);
            this.context = context;
            this.subs = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.subscription_info_view, null);
            }

            Subscription mv = subs.get(position);


            ArrayList<String> categories = mv.getmCategories();
                ArrayList<String> tags = mv.getmTags();
                String list = "";
                for (int i = 0; i < categories.size(); i++) {
                    list = list + " " + categories.get(i) + ", ";
                }
                for (int i = 0; i < tags.size(); i++) {
                    if (i < (tags.size() - 1)) {
                        list = list + " " + tags.get(i) + ", ";
                    } else {
                        list = list + " " + tags.get(i);

                    }
                }

                if (list.length() > 100) {
                    list = list.substring(0, 100);
                    list += " ...";
                }
                TextView detail = (TextView) convertView.findViewById(R.id.viewSubscriptionInfo__detail);
                detail.setText(list);

            return convertView;
        }
    }

}
