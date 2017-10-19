package jamilaappinc.grubmate;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostsFragment extends Fragment {
    private ListView list;
    private PostAdapter adapter;
    private ArrayList<Post> posts = new ArrayList<>();
    android.support.design.widget.FloatingActionButton floatButton;
    int _position;

    private String ID;




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
        View v = inflater.inflate(R.layout.fragment_my_posts, container, false);
        Intent i = getActivity().getIntent();
        ID = i.getStringExtra("ID");
        Toast.makeText(getContext(), "@JAMILAAPPCORP: FOUND ID  "+ ID , Toast.LENGTH_SHORT).show();
        initComponents(v);
        if(posts!=null){
            list.setAdapter(adapter);
        }
        addListeners();
        return v;
    }

    private void initComponents(View v){
        posts = (ArrayList<Post>)getArguments().getSerializable(MyPostsActivity.GET_POSTS);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        list = (ListView) v.findViewById(R.id.mypost_list);
        adapter= new PostAdapter(getActivity());

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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _position = position;
                Post post = (Post) list.getItemAtPosition(position);
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Complete Transaction");
                adb.setMessage("Completed delivery for" + post.getmTitle() + "?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        posts.remove(_position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO SEND NOTIFICATION TO ACCEPTED USERS" +
                                " and mark post as inactive" , Toast.LENGTH_SHORT).show();
                    }});
                adb.show();

            }
        });
    }

    private class PostAdapter extends ArrayAdapter<Post> {
        public PostAdapter(Context context){
            super(context, 0 , posts);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.mypost_info_view, parent, false);

            }


            Post post = posts.get(position);
            String title = post.getmTitle();

            TextView detail = (TextView) itemView.findViewById(R.id.mypost_info__detail);
            detail.setText(title);
            return itemView;
        }
    }


}
