package mau.resturantapp.aktivitys.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;

/**
 * Created by AnwarC on 15/11/2016.
 */

public class Dialog_login extends DialogFragment implements View.OnClickListener{

    private View rod;
    private Button facebookBtn;
    private EditText UIusername;
    private EditText UIpassword;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       // LayoutInflater inflater = LayoutInflater.from(getActivity());
        //View root = inflater.inflate(R.layout.dialog_login,(ViewGroup) getActivity().findViewById(android.R.id.content));
        //builder.setView(root);
        builder.setTitle("Log in");
        builder.setNeutralButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    loginButtonClick();
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    cancelButtonClick();
            }
        });
        builder.setNegativeButton("Sign up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    signupButtonClick();
            }
        });

        rod = View.inflate(getContext(),R.layout.dialog_login,null);
        facebookBtn = (Button) rod.findViewById(R.id.btn_facebookLogin);
        UIusername = (EditText) rod.findViewById(R.id.UITxt_dialog_username);
        UIpassword = (EditText) rod.findViewById(R.id.UITxt_dialog_password);

        builder.setView(rod);
        AlertDialog create = builder.create();

        return create;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void cancelButtonClick(){
        //nothing in here, closes by it self.
    }
    private void signupButtonClick(){
        appData.event.showSignupDialog();
    }
    private void loginButtonClick(){
        validLogin();
    }

    private void validLogin() {
        String email = UIusername.getText().toString();
        String password = UIpassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Venligst udfyld både e-mail og pasord", Toast.LENGTH_LONG).show();
            appData.event.showLoginDialog();
        }

        else{
            //appData.validLogin(email,password);
            appData.testValidLogin(email,password);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == facebookBtn){
            // TODO: facebook login
        }
    }
}
