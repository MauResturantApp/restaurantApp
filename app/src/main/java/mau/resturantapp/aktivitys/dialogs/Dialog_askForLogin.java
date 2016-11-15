package mau.resturantapp.aktivitys.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;

/**
 * Created by AnwarC on 15/11/2016.
 */

public class Dialog_askForLogin extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Log in");
        builder.setMessage("Du kan logge ind, eller tilmelde dig for hurtigere checkouts til dine fremtidige ordre");
        builder.setNeutralButton("Nej tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    guestUserClicked();
            }
        });
        builder.setPositiveButton("Log in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    loginClicked();
            }
        });
        builder.setNegativeButton("Tilmeld", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                signupCLicked();
            }
        });

        AlertDialog create = builder.create();

        return create;
    }

    private void guestUserClicked(){
        appData.event.guestCheckout();
    }
    private void loginClicked(){
        appData.event.showLoginDialog();
    }

    private void signupCLicked(){
        appData.event.showSignupDialog();
    }

}