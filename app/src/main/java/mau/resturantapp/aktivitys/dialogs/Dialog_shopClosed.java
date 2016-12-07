package mau.resturantapp.aktivitys.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import mau.resturantapp.data.appData;

/**
 * Created by AnwarC on 07/12/2016.
 */

public class Dialog_shopClosed extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Shop is closed");
        builder.setMessage("Our oppening hours: " + appData.OPENHOUR +":"+appData.OPENMINUT);
        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        AlertDialog dialog = builder.create();

        return dialog;
    }
}
