package mau.resturantapp.aktivitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import mau.resturantapp.R;

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
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_down_out1);
        Fragment frag = new MenuTabs_frag();
        Log.d("succe", "login");
        transaction.replace(R.id.mainFrameFrag, frag);

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
