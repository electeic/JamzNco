package jamilaappinc.grubmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    TextView fHome;
    TextView fProfile;
    TextView fNotifications;
    TextView fPost;
    TextView fGroups;
    TextView fSubscriptions;
    TextView fSearch;
    TextView fLogout;
    TextView fCreateSubscription;
    TextView fMyPosts;



    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Toast.makeText(getActivity().getApplicationContext(), "Click on an option to be redirected to that page!.", Toast.LENGTH_LONG).show();

        Intent i = getActivity().getIntent();
        final String id = i.getStringExtra("ID");

        fHome = (TextView)v.findViewById(R.id.home);
        fProfile = (TextView)v.findViewById(R.id.profile);
        fNotifications = (TextView)v.findViewById(R.id.notifications);
        fPost = (TextView)v.findViewById(R.id.post);
        fGroups = (TextView)v.findViewById(R.id.groups);
        fSubscriptions = (TextView)v.findViewById(R.id.subscriptions);
        fSearch = (TextView)v.findViewById(R.id.search);
        fLogout = (TextView)v.findViewById(R.id.logout);
        fCreateSubscription = (TextView)v.findViewById(R.id.createSubscription);
        fMyPosts =(TextView)v.findViewById(R.id.myPosts);

        fHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("ID", id);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });
        fCreateSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), createsSubscriptionActivity.class);
                intent.putExtra("ID", id);
                startActivityForResult(intent, 0);
            }
        });

        fProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("ID", id);
                System.out.println("IN MENU FRAGMENT, ID IS" + id);
                startActivityForResult(intent, 0);
            }
        });

        fNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewNotificationsActivity.class);
                intent.putExtra("ID", id);

                startActivityForResult(intent, 0);
            }
        });
        fMyPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyPostsActivity.class);
                ArrayList<Post> posts = new ArrayList<Post>();
                for(int i =0; i < 5; i++){
                    posts.add(new Post("Post " + i, i));
                }
                intent.putExtra(MyPostsActivity.GET_POSTS, posts);

                startActivityForResult(intent, 0);
            }
        });

        fPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra("ID", id);

                startActivityForResult(intent, 0);
            }
        });

        fGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewGroupsActivity.class);
                intent.putExtra("ID", id);

                startActivityForResult(intent, 0);
            }
        });

        fSubscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewSubscriptionsActivity.class);
                intent.putExtra("ID", id);

                startActivityForResult(intent, 0);
            }
        });

        fSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        fLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });



        return v;
    }

}
