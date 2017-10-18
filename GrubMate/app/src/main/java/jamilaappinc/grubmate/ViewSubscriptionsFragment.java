package jamilaappinc.grubmate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

    /*
        THIS IS FOR DYNAMIC MAYBE NOT REALLY SURE
        private ArrayList<Subscription> subscriptions;

        IF IT IS THEN DELETE THE BOTTOM ONE
     */
    private ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();


//    private OnFragmentInteractionListener mListener;

    public ViewSubscriptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewSubscriptionsFragment.
     */
    // TODO: Rename and change types and number of parameters


    /*
        THIS IS FOR DYNAMIC
        public static ViewSubscriptionsFragment newInstance(ArrayList<Subscription> subscriptions){
     */
    public static ViewSubscriptionsFragment newInstance(int pos) {
        ViewSubscriptionsFragment fragment = new ViewSubscriptionsFragment();
        Bundle args = new Bundle();
        /*
            THIS IS FOR DYNAMIC
             args.putInt(ViewSubscriptionsActivity.GET_SUBSCRIPTION, subscriptions);

         */
        args.putInt(ViewSubscriptionsActivity.GET_SUBSCRIPTION, pos);
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
        populateList();
        initComponents(v);
        addListeners();;
        list.setAdapter(adapter);


        return v;
    }
    private void initComponents(View v){
        /*
            subscriptions = (ArrayList<Subscription>)getArguments().getSerializable(ViewSubscriptionsActivity.GET_SUBSCRIPTION);
         */

        adapter = new SubscriptionAdapter(getActivity());
        list = (ListView) v.findViewById(R.id.viewSubscriptions_list);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
    }

    private void addListeners(){
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivityForResult(intent, 0);
                getActivity().finish();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Subscription group = (Subscription) list.getItemAtPosition(position);
                subscriptions.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "@JAMILAAPPCORP: NEED TO DELETE SUBSCRIPTION FROM DB" , Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * NOTE: I added another constructer into Subscription to make testing easier
     *
     */
    public void populateList(){
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            categories.add("Category " + i);
            tags.add("Tag " + i);
        }
        subscriptions.add(new Subscription(tags,categories));
        subscriptions.add(new Subscription(tags,categories));
        subscriptions.add(new Subscription(tags,categories));
        subscriptions.add(new Subscription(tags,categories));
        subscriptions.add(new Subscription(tags,categories));
        subscriptions.add(new Subscription(tags,categories));
        subscriptions.add(new Subscription(tags,categories));

    }

    private class SubscriptionAdapter extends ArrayAdapter<Subscription> {
        public SubscriptionAdapter(Context context){
            super(context, 0 , subscriptions);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.subscription_info_view, parent, false);

            }


            Subscription subscription = subscriptions.get(position);
            ArrayList<String> categories = subscription.getmCategories();
            ArrayList<String> tags = subscription.getmTags();
            String list = "";
            for(int i = 0; i < categories.size(); i++){
                list = list+" " + categories.get(i)+", ";
            }
            for(int i=0; i< tags.size(); i++){
                if(i < (tags.size() -1)){
                    list = list+" " + tags.get(i)+", ";
                }else{
                    list = list+" " + tags.get(i);

                }
            }

            if(list.length() > 100){
                list = list.substring(0, 100);
                list+=" ...";
            }
            TextView detail = (TextView) itemView.findViewById(R.id.viewSubscriptionInfo__detail);
            detail.setText(list);
            return itemView;
        }
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
