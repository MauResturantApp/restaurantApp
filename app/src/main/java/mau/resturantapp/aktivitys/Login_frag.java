package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.data.appData;
import mau.resturantapp.events.LoggedInEvent;
import mau.resturantapp.events.OnFailedLogIn;
import mau.resturantapp.events.OnSuccesfullLogInEvent;

/**
 * Created by anwar on 10/16/16.
 */

public class Login_frag extends Fragment implements View.OnClickListener {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button logInButton;
    protected TextView signUpTextView;
    private ProgressBar loader;
    private View rod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.login_frag, container, false);



        loader = (ProgressBar) rod.findViewById(R.id.progBar_logIn);
        signUpTextView = (TextView) rod.findViewById(R.id.signUpText);
        emailEditText = (EditText) rod.findViewById(R.id.emailField);
        passwordEditText = (EditText) rod.findViewById(R.id.passwordField);
        logInButton = (Button) rod.findViewById(R.id.loginButton);
        loader.setVisibility(View.GONE);

        logInButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
        EventBus.getDefault().register(this);

        return rod;
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);


        super.onResume();
    }

    @Override
    public void onClick(View v) {

        if (v == logInButton) {
            loader.setVisibility(View.VISIBLE);
            validLogin();
        }
        if (v == signUpTextView) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment frag = new Signup_frag();
            ft.replace(R.id.mainFrameFrag, frag).commit();

        }


    }


    private void validLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Venligst udfyld både e-mail og pasord", Toast.LENGTH_LONG).show();
            loader.setVisibility(View.GONE);

        }

        else{
            appData.validLogin(email,password);
        }
    }

    @Subscribe
    public void onSuccesLogin(LoggedInEvent event){
        loader.setVisibility(View.GONE);
    }

    @Subscribe
    public void onFailedLogin(OnFailedLogIn event){
        loader.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Forkert brugernavn eller password", Toast.LENGTH_LONG).show();


    }



}
