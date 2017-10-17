package jamilaappinc.grubmate;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by ericajung on 10/16/17.
 */



/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public static final String ARGS_POSITION = "args_position";

    //    private List<Post> postList;
    //    private PostListAdapter postAdapter;
    android.support.design.widget.FloatingActionButton floatButton;
    ListView mListView;
    public static final String IDString = "fuck";


    FirebaseListAdapter mAdapter;
    FirebaseDatabase database;
    DatabaseReference dbRefPosts;
    String currUserId;
    String currUserName;
    ArrayList<String> userFriends;

    public SearchFragment() {
        // Required empty public constructor
    }

    //TODO modify for id
    public static SearchFragment newInstance(int pos) {
        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, pos);
        SearchFragment f = new SearchFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        //todo get database
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);
        //todo get database reference paths
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_filtered_search, container, false);



        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        //find views
        mListView = (ListView)v.findViewById(R.id.active_post_list);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int position, long id) {
                // we know a row was clicked but we need to know WHERE specifically
                // is that data stored in the database
                DatabaseReference dbRefClicked = mAdapter.getRef(position);
                Intent i = new Intent(getActivity(), DetailedPostActivity.class);
                // toString instead of sending over the whole DatabaseReference because it's easier
                i.putExtra(DetailedPostActivity.EXTRA_URL, dbRefClicked.toString());
                startActivity(i);
            }
        });

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID",currUserId);
                startActivityForResult(intent, 0);
                //                getActivity().finish();
            }
        });

// Attach a listener to read the data at our posts reference
        dbRefPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



        return v;
    }
}
