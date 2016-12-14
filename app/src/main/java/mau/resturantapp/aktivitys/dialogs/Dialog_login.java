package mau.resturantapp.aktivitys.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;
import mau.resturantapp.utils.Firebase.FirebaseAuthentication;

/**
 * Created by AnwarC on 15/11/2016.
 */

public class Dialog_login extends DialogFragment {

    private View rod;
    private LoginButton facebookBtn;
    private EditText UIusername;
    private EditText UIpassword;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
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
        UIusername = (EditText) rod.findViewById(R.id.UITxt_dialog_username);
        UIpassword = (EditText) rod.findViewById(R.id.UITxt_dialog_password);

        callbackManager = CallbackManager.Factory.create();
        facebookBtn = (LoginButton) rod.findViewById(R.id.btn_facebookLogin);
        facebookBtn.setFragment(this);
        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                FirebaseAuthentication.loginFacebook(loginResult.getAccessToken());
                dismiss();
            }

            @Override
            public void onCancel() {
                Log.d("Dialog_login", "Facebook button cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.w("Dialog_login", "Facebook on error exception: " + exception);
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    LoginManager.getInstance().logOut();
                    FirebaseAuthentication.logOutUser();
                }
            }
        };
        accessTokenTracker.startTracking();

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
            FirebaseAuthentication.ValidLogin(email,password);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
