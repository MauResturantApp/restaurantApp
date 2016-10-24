package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import mau.resturantapp.R;
import mau.resturantapp.events.OnSuccesfullLogInEvent;

/**
 * Created by anwar on 10/16/16.
 */

public class Login_frag extends Fragment implements View.OnClickListener, OnCompleteListener {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button logInButton;
    protected TextView signUpTextView;
    private FirebaseAuth mFirebaseAuth;
    private ProgressBar loader;
    private View rod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rod = inflater.inflate(R.layout.login_frag, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();


        loader = (ProgressBar) rod.findViewById(R.id.progBar_logIn);
        signUpTextView = (TextView) rod.findViewById(R.id.signUpText);
        emailEditText = (EditText) rod.findViewById(R.id.emailField);
        passwordEditText = (EditText) rod.findViewById(R.id.passwordField);
        logInButton = (Button) rod.findViewById(R.id.loginButton);
        loader.setVisibility(View.GONE);

        logInButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);

        return rod;
    }

    @Override
    public void onClick(View v) {

        if (v == logInButton) {
            loader.setVisibility(View.VISIBLE);
            //loading
            validLogin();
        }
        if (v == signUpTextView) {


        }


    }

    private void validLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        email = email.trim();
        password = password.trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Venligst udfyld både e-mail og pasord", Toast.LENGTH_LONG).show();


        } else {
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this);
        }
    }


    private void onSuccesfullLogin() {
        OnSuccesfullLogInEvent event = new OnSuccesfullLogInEvent();
        EventBus.getDefault().post(event);
        Log.d("Logged in", "sending to home");
    }

    @Override
    public void onComplete(@NonNull Task task) {
        loader.setVisibility(View.GONE);
        if (task.isSuccessful()) {
            onSuccesfullLogin();
        } else {
            Toast.makeText(getContext(), "pasord eller e-mail er forkert, prøv igen", Toast.LENGTH_LONG).show();


        }
    }


}
