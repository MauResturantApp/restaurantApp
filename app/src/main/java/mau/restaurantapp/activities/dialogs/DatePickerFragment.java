package mau.restaurantapp.activities.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    private Fragment mFrag;

    public DatePickerFragment(Fragment mFrag) {
        this.mFrag = mFrag;
    }

    @Override
    @Nullable
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) mFrag, year, month, day);
    }
}