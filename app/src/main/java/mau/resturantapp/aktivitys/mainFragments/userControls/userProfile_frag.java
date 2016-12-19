package mau.resturantapp.aktivitys.mainFragments.userControls;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import mau.resturantapp.R;
import mau.resturantapp.data.UserProfile;
import mau.resturantapp.data.appData;
import mau.resturantapp.utils.Firebase.FirebaseWrite;

public class userProfile_frag extends Fragment implements View.OnClickListener {

    private EditText name;
    private EditText phoneNumber;
    private TextView emailAddress;
    private Button updateButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rod =  inflater.inflate(R.layout.user_profile_frag, container, false);

        name = (EditText) rod.findViewById(R.id.name_editText);
        phoneNumber = (EditText) rod.findViewById(R.id.phoneNumber_edittext);
        emailAddress = (TextView) rod.findViewById(R.id.emailAddress_editText);

        updateUI();
        emailAddress.setText(appData.firebaseAuth.getCurrentUser().getEmail());

        updateButton = (Button) rod.findViewById(R.id.updateProfile);
        updateButton.setOnClickListener(this);
        return rod;
    }

    @Override
    public void onClick(View v) {
        if(v == updateButton){
            //testing
            sendTokenToServer("ajjafjksgjrKKoooPPadadjk");
            if(hasUpdate()){
                FirebaseWrite.updateUserProfile(name.getText().toString(), phoneNumber.getText().toString());
            }
        }
    }

    private boolean hasUpdate(){
        UserProfile userProfile = appData.userProfile;
        if(userProfile != null){
            if(userProfile.getName() != null && userProfile.getPhoneNumber() != null){
                if(userProfile.getName().equals(name.getText().toString()) && userProfile.getPhoneNumber().equals(phoneNumber.getText().toString())){
                    return false;
                }
            }
        }

        return true;
    }

    private void updateUI(){
        UserProfile userProfile = appData.userProfile;
        if(userProfile != null){
            if(userProfile.getName() != null && userProfile.getPhoneNumber() != null){
                name.setText(userProfile.getName());
                phoneNumber.setText(userProfile.getPhoneNumber());
            }
        }
    }

    /**
     * Testing connection to server - Should not be in here in final build
     * @param token represents the real device token
     */
    public void sendTokenToServer(String token){
        testServerConnection test = new testServerConnection();
        test.token = token;
        test.execute();
    }

    private class testServerConnection extends AsyncTask<Void, Void, Void> {

        private String token;

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection connection = null;
            try {
                //Create connection

                URL url = new URL("http://:9000");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length",
                        Integer.toString(token.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream (
                        connection.getOutputStream());
                wr.writeBytes(token);
                wr.close();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Log.d("TOKEN TO SERVER", response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }
    }
}
