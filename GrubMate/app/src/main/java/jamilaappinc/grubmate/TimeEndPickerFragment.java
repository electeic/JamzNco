package jamilaappinc.grubmate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

/**
 * Created by melod on 10/15/2017.
 */

public class TimeEndPickerFragment extends DialogFragment {
    private TimePicker timePicker;
    public interface TimeEndDialogListener {
        void onFinishEndDialog(String time);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time,null);

        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Select a Ending Time")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour = 0;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    hour = timePicker.getHour();
                                }else{
                                    hour = timePicker.getCurrentHour();
                                }
                                int minute = 0;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    minute = timePicker.getMinute();
                                }else{
                                    minute = timePicker.getCurrentMinute();
                                }
                                TimeEndDialogListener activity = (TimeEndDialogListener) getActivity();
                                activity.onFinishEndDialog(updateTime(hour,minute));
                                dismiss();
                            }
                        })
                .create();
    }

    private String updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String myTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return myTime;
    }
}

