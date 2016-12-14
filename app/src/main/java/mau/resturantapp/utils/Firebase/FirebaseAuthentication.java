package mau.resturantapp.utils.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import mau.resturantapp.data.CartContent;
import mau.resturantapp.data.Product;
import mau.resturantapp.data.appData;
import mau.resturantapp.user.LoggedInUser;

/**
 * Created by Yoouughurt on 14-12-2016.
 */

public class FirebaseAuthentication {
    private static void onSuccesfullLogin() {
        String name = appData.firebaseAuth.getCurrentUser().getDisplayName();
        String email = appData.firebaseAuth.getCurrentUser().getEmail();
        appData.currentUser = new LoggedInUser(name, email, 0);
        FirebaseRead.getUserProfile();
        isAdmin();
    }

    public static void isAdmin() {
        DatabaseReference ref = appData.firebaseDatabase.getReference("permissions/" + appData.firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    appData.currentUser.setAdmin(true);
                    Log.d("Logged in as admin : ", "" + appData.currentUser.isAdmin());
                    appData.event.isAdmin();
                }
                //Succesfull Login used to clean up recyclerviewAdapter in CartContentFirebase
                appData.event.succesfullLogin();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Admin check error", databaseError.getMessage());
            }
        });
    }

    public static void NewUser(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        linkWithCredential(credential);
    }

    public static void ValidLogin(final String userEmail, final String userPassword) {
        FirebaseUser user = appData.firebaseAuth.getCurrentUser();
        if (user != null && user.isAnonymous()) {
            saveShoppingCart();
        } else {
            Log.d("prepare", "for login");
            prepareForLogin();
        }

        LoginAndTransfer(userEmail, userPassword);
    }

    private static void LoginAndTransfer(String userEmail, String userPassword) {

        String email = userEmail;
        String password = userPassword;

        email = email.trim();
        password = password.trim();

        appData.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            transferShoppingCart();
                            onSuccesfullLogin();
                        } else {
                            Log.d("Fail Login Exception ", ""+task.getException());
                            appData.event.failedLogin();
                        }

                    }
                });
    }

    public static void logOutUser() {
        if(!appData.loggingIn) {
            LoginManager.getInstance().logOut();
        }
        appData.firebaseAuth.signOut();
        appData.currentUser = null;
        Log.d("Appdata", "Logged out user");
    }

    public static void logInAnonymously() {
        appData.firebaseAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Authstate", "signInAnonymously:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("AuthState", "signInAnonymously", task.getException());
                            appData.event.failedLogin();
                        } else {
                            appData.currentUser = new LoggedInUser();
                            appData.event.succesfullLogin();
                        }
                    }
                });
    }

    public static void loginFacebook(AccessToken token) {
        Log.d("appData", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        linkWithCredential(credential);
    }

    private static void linkWithCredential(final AuthCredential credential){
        appData.firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LinkWithCredential", "SUCCESS");
                            appData.event.newUserSuccesfull();
                        } else {
                            loginWithCredential(credential);
                        }
                    }
                });
    }

    private static void loginWithCredential(AuthCredential credential){
        FirebaseUser user = appData.firebaseAuth.getCurrentUser();
        if (user != null && user.isAnonymous()) {
            saveShoppingCart();
        } else {
            prepareForLogin();
        }

        appData.firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("appData", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("appData", "signInWithCredential", task.getException());
                            appData.event.failedLogin();
                        } else {
                            transferShoppingCart();
                            onSuccesfullLogin();
                        }
                    }
                });

    }

    private static void saveShoppingCart(){
        final DatabaseReference ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.firebaseAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Product> cartContent = new HashMap<>();
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        String key = productSnapshot.getKey();
                        Product product = productSnapshot.getValue(Product.class);
                        Log.d("CartContent: ", "key " + key + " name " + product.getName() + " price " + product.getPrice());
                        cartContent.put(key, product);
                    }
                    appData.shoppingCart = new CartContent(cartContent);
                    ref.removeValue();
                }
                prepareForLogin();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("save shoppingCart error", databaseError.getMessage());
            }
        });
    }

    private static void transferShoppingCart(){
        if (appData.shoppingCart != null) {
            DatabaseReference ref = appData.firebaseDatabase.getReference("shoppingcart/" + appData.firebaseAuth.getCurrentUser().getUid());
            ref.setValue(appData.shoppingCart.getCartContent());
            appData.shoppingCart = null;
        }
    }

    /**
     * Function that prepares for a login from Anonymous user to Authenticated user.
     * In order for this transition to work you need to log out with the anonymous user first.
     * Since we automatically login users anonymously the boolean loggingIn set to true is needed,
     * to stay signed out while we log in to the authenticated user.
     */
    private static void prepareForLogin(){
        appData.loggingIn = true;
        logOutUser();
    }
}
