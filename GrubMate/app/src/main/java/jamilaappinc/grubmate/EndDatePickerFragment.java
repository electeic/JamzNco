package jamilaappinc.grubmate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by melod on 10/15/2017.
 */

public class EndDatePickerFragment extends DialogFragment {
    private DatePicker datePicker;


    public interface EndDateDialogListener {
        void onFinishEndDialog(Date date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);
        datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Choose a day")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = datePicker.getYear();
                                int mon = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, mon, day).getTime();
                                EndDateDialogListener activity = (EndDateDialogListener) getActivity();
                                activity.onFinishEndDialog(date);
                                dismiss();
                            }
                        })
                .create();
    }
}