package mau.restaurantapp.activities.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import mau.restaurantapp.data.AppData;

/**
 * Created by AnwarC on 07/12/2016.
 */

public class ShopClosed extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Shop is closed");
        builder.setMessage("Our oppening hours: " + AppData.OPENHOUR + ":" + AppData.OPENMINUT);
        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        AlertDialog dialog = builder.create();

        return dialog;
    }
}
