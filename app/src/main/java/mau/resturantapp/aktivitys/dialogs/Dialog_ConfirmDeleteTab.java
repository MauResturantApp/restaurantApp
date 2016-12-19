package mau.resturantapp.aktivitys.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import mau.resturantapp.data.appData;

/**
 * Created by AnwarC on 17/12/2016.
 */

public class Dialog_ConfirmDeleteTab extends DialogFragment {

    private String id;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("!!!");
        builder.setMessage("Ved at slette en menu Tab, sletter du også alt indholdet, vil du fortsætte?");

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        try {
            id = savedInstanceState.getString("id");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    appData.event.confirmDeleteTab(id);
                }
            });
        }
        catch (NullPointerException n){
            Log.d("null fejl ----------","b " + n);
        }


        AlertDialog create = builder.create();

        return create;
    }
}