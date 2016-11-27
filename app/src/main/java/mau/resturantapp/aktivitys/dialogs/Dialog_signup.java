package mau.resturantapp.aktivitys.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;

/**
 * Created by AnwarC on 15/11/2016.
 */

public class Dialog_signup extends DialogFragment {

    private View rod;
    private EditText UIEmail;
    private EditText UIPassword;
    private EditText UIPasswordCheck;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Udfyld felterne");
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("Sign up!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    signupClicked();
            }
        });


        rod = View.inflate(getContext(), R.layout.dialog_signup, null);
        UIEmail = (EditText) rod.findViewById(R.id.UITxt_dialog_signup_username);
        UIPassword = (EditText) rod.findViewById(R.id.UITxt_dialog_signup_password);
        UIPasswordCheck = (EditText) rod.findViewById(R.id.UITxt_dialog_signup_passwordCheck);


        builder.setView(rod);

        AlertDialog create = builder.create();

        return create;
    }

    private void cancelClicked(){
        // do nothing. dialogs closes by itself.
    }
    private void signupClicked(){
        String password = UIPassword.getText().toString();
        String email = UIEmail.getText().toString();
        String passWordCheck = UIPasswordCheck.getText().toString();

        password = password.trim();
        email = email.trim();
        passWordCheck = passWordCheck.trim();

        if (password.isEmpty() || email.isEmpty() || passWordCheck.isEmpty()) {
            Toast.makeText(getContext(), "Venligst udfyld alle felter", Toast.LENGTH_LONG).show();
            appData.event.showSignupDialog();

        } else if (!password.equals(passWordCheck)) {
            Toast.makeText(getContext(), "De indtastede password matcher ikke, venligst prøv igen", Toast.LENGTH_LONG).show();
            appData.event.showSignupDialog();

        } else {
            appData.testNewUser(email,password);
        }
    }
}