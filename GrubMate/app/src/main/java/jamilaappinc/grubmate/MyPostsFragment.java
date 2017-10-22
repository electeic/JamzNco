package jamilaappinc.grubmate;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostsFragment extends Fragment {
    private ListView list;
//    private PostListAdapter adapter;
    private ArrayList<Post> mPosts = new ArrayList<>();
    android.support.design.widget.FloatingActionButton floatButton;
//    HashSet<String>
    int _position;

    FirebaseDatabase database;
    DatabaseReference dbRefMyPosts;
    DatabaseReference dbRefPosts;
    private String ID;
    ArrayList<Integer> postsReadCounter = new ArrayList<>();
    ArrayList<Integer> postCount = new ArrayList<>();

    MovieAdapter mAdapter;

    public MyPostsFragment() {
        // Required empty public constructor
    }

    public static MyPostsFragment newInstance(ArrayList<Post> posts){
        MyPostsFragment fragment = new MyPostsFragment();
        Bundle args = new Bundle();
        args.putSerializable(MyPostsActivity.GET_POSTS, posts);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_my_posts, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        dbRefMyPosts = database.getInstance().getReference().child(FirebaseReferences.USERS).child(FirebaseReferences.MYPOSTS);
        dbRefPosts = database.getInstance().getReference().child(FirebaseReferences.POSTS);

        postsReadCounter.add(0);

        database.getReference().addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) { //goes through posts, find the number of posts
                    Log.e(snap.getKey() + " GETTING NUM KEYS",snap.getChildrenCount() + "");
                    if (snap.getKey().equals("Post")) { //if it
                        postCount.add((int)snap.getChildrenCount());
                        System.out.println("ADDED # FRIENDS, count is " + snap.getChildrenCount());
                    }
                }
                dbRefPosts.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        int postsRead = postsReadCounter.get(0);
                        postsRead++;
                        System.out.println("POSTS READ COUNT " + postsRead);
                        postsReadCounter.clear();
                        postsReadCounter.add(postsRead);


                        Post post = dataSnapshot.getValue(Post.class);

                        if(post.getmAuthorId().equals(ID)){
                            mPosts.add(post);
                            System.out.println("I GOT A POST!!" + post);
                        }

                        System.out.println("POSTS READ COUNTER" + postsReadCounter.get(0));
                        System.out.println("POSTS COUNTER" + postCount.get(0));

                        if(postsReadCounter.get(0) == postCount.get(0)){
                            System.out.println("IN IF, SETTING ADAPTER NOW");
                            mAdapter = new MovieAdapter(getActivity(), R.layout.list_active_posts_item, mPosts);
                            //        postList = PostSingleton.get(getActivity()).getMovies();
                            //        postAdapter = new PostListAdapter(getActivity(), Post.class,
                            //                R.layout.list_active_posts_item,
                            //                dbRefPosts);
                            list.setAdapter(mAdapter);
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

//        database.getInstance().getReference().child("users")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            Post myPost = snapshot.getValue(Post.class);
//                            posts.add(myPost);
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//
//        // Attach a listener to read the data at our posts reference
//        dbRefMyPosts.addValueEventListener(new ValueEventListener(){
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String post = dataSnapshot.getValue(String.class);
//                    System.out.println(post);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });



        initComponents(v);
//        if(posts!=null){
//            list.setAdapter(adapter);
//        }
        addListeners();
        return v;
    }

    private void initComponents(View v){
//        mPosts = (ArrayList<Post>)getArguments().getSerializable(MyPostsActivity.GET_POSTS);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        list = (ListView) v.findViewById(R.id.mypost_list);
//        adapter= new PostListAdapter(getActivity(), Post.class, R.layout.list_active_posts_item, dbRefMyPosts);

    }

    private void addListeners() {
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  /*              Intent i = getActivity().getIntent();
                ID = i.getStringExtra("ID");*/
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });
    }

    public class MovieAdapter extends ArrayAdapter<Post> {
        List<Post> Posts;
        Context context;

        public MovieAdapter(Context context, int resource, List<Post> objects) {
            super(context, resource, objects);
            this.context = context;
            this.Posts = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.list_active_posts_item, null);
            }

            ImageView image = (ImageView) convertView.findViewById(R.id.imagePic);
            ImageView imagePerson = (ImageView) convertView.findViewById(R.id.active_post_person_image);
            TextView textTitle = (TextView) convertView.findViewById(R.id.listNoteTitle);
            TextView textDescription = (TextView) convertView.findViewById(R.id.listNoteContent);

            //            findOutMore.setTag(position);
            //            findOutMore.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    Intent i = new Intent(getActivity(),
            //                            DetailActivity.class);
            //
            //                    //TODO change position to id
            ////                i.putExtra(DetailActivity.EXTRA_POSITION, position);
            //                    i.putExtra(DetailActivity.ARGS_ID, (int) v.getTag());
            //                    startActivityForResult(i, 0);
            //                }
            //            });



            //            Movies = MovieSingleton.get(context).getMovies();
            Post mv = Posts.get(position);

            Glide.with(MyPostsFragment.this)
                    .load( mv.getmPhotos())
                    .centerCrop()
                    .placeholder(R.drawable.hamburger)
                    .crossFade()
                    .into(image);

//                image.setImageDrawable(getResources().getDrawable(R.drawable.gmlogo));


            textTitle.setText(mv.getmTitle());
            textDescription.setText(mv.getmDescription());
            return convertView;
        }
    }

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                _position = position;
//                Post post = (Post) list.getItemAtPosition(position);
//                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
//                adb.setTitle("Complete Transaction");
//                adb.setMessage("Completed delivery for" + post.getmTitle() + "?");
//                adb.setNegativeButton("Cancel", null);
//                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        posts.remove(_position);
//                        adapter.notifyDataSetChanged();
//                        Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO SEND NOTIFICATION TO ACCEPTED USERS" +
//                                " and mark post as inactive" , Toast.LENGTH_SHORT).show();
//                    }});
//                adb.show();
//
//            }
//        });


//    private class PostListAdapter extends FirebaseListAdapter<Post> {
//
//        public PostListAdapter(Activity activity, Class<Post> modelClass, int modelLayout, DatabaseReference ref) {
//            super(activity, modelClass, modelLayout, ref);
//        }
//
////        private PostFilter postFilter;
//
//        @Override
//        protected void populateView(View v, Post model, int position) {
//            // get references to row widgets
//            // copy data from model to widgets
//            boolean isFriendPost = false;
//            //
//
//            ImageView mImage = (ImageView)v.findViewById(R.id.imagePic);
//
//            Glide.with(MyPostsFragment.this)
//                    .load(model.getmPhotos())
//                    .centerCrop()
//                    .placeholder(R.drawable.hamburger)
//                    .crossFade()
//                    .into(mImage);
//
//            TextView pPostContent = (TextView)v.findViewById(R.id.listNoteContent);
//            TextView pPostTitle = (TextView)v.findViewById(R.id.listNoteTitle);
//
//            pPostContent.setText(model.getmDescription());
//            pPostTitle.setText(model.getmTitle());
//        }
//    }

}
