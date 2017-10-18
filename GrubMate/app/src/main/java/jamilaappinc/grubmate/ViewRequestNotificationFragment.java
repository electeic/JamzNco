package jamilaappinc.grubmate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRequestNotificationFragment extends Fragment {

    android.support.design.widget.FloatingActionButton floatButton;
    private Notification notification;
    private TextView name, title, size, location;
    private Button accept, deny;

    public ViewRequestNotificationFragment() {
        // Required empty public constructor
    }
    public static ViewRequestNotificationFragment newInstance(Notification request) {
        Bundle args = new Bundle();
        ViewRequestNotificationFragment fragment = new ViewRequestNotificationFragment();
        args.putSerializable(ViewRequestNotificationActivity.GET_REQUEST, request);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_request_notification, container, false);
        Toast.makeText(getActivity().getApplicationContext(), "Here you can accept or deny the request!", Toast.LENGTH_LONG).show();
        initComponents(v);
        addListeners();
        fillInScreen();


        return v;
    }

    private void fillInScreen(){
        name.setText(notification.getmFromUser().getName());
        title.setText(notification.getmAboutPost().getmTitle());
        size.setText(notification.getmAboutPost().getmServings()+"");
        location.setText(notification.getmAboutPost().getmLocation());
    }

    private void initComponents(View v){
        notification = (Notification) getArguments().getSerializable(ViewRequestNotificationActivity.GET_REQUEST);
        floatButton = (android.support.design.widget.FloatingActionButton) v.findViewById(R.id.menu_from_main);
        name = (TextView) v.findViewById(R.id.requestNotif_userName);
        title = (TextView) v.findViewById(R.id.requestNotif_postTitle);
        size =(TextView)v.findViewById(R.id.requestNotif_servingAmt);
        location = (TextView) v.findViewById(R.id.requestNotif_locationAddr);
        accept = (Button) v.findViewById(R.id.requestNotif_acceptButton);
        deny =(Button) v.findViewById(R.id.requestNotif_denyButton);
    }

    private void addListeners() {
        floatButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivityForResult(intent, 0);
                        getActivity().finish();
                    }
                }
        );
        accept.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "@JAMILAAPPCORP:(VIEW REQUEST NOTIF) SEND THE CORRESPONDING USER A NOTIFICATION AND ADD TO POST'S ACCEPTED USERS ARRAYLIST  AND DELETE NOTIFICATION FROM USER" , Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), ViewNotificationsActivity.class);
                        startActivity(i);


                    }
                }
        );

        deny.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "@JAMILAAPPCORP:(VIEW REQUEST NOTIF)  DELETE NOTIFICATION FROM USER" , Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), ViewNotificationsActivity.class);
                        startActivity(i);
                    }
                }
        );

    }


}
