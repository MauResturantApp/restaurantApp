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


    /**
     *  Handles the necessary actions to take when an authenticated user signs in.
     */
    private static void onSuccesfullLogin() {
        String name = appData.firebaseAuth.getCurrentUser().getDisplayName();
        String email = appData.firebaseAuth.getCurrentUser().getEmail();
        appData.currentUser = new LoggedInUser(name, email, 0);
        FirebaseRead.getUserProfile();
        isAdmin();
    }

    /**
     * Checks if the currently logged in user is an administrator.
     * If he is an administrator this will be saved and an event to handle it will fire.
     */
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

    /**
     * Handles the sign up flow with email authentication.
     * @param email The users email
     * @param password The users password
     */
    public static void NewUser(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        linkWithCredential(credential);
    }

    /**
     *  Handles the login flow for email authentication.
     * @param userEmail The users email
     * @param userPassword The users password
     */
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

    /**
     *  Signs in to firebase and transfers their shoppingcart to firebase.
     * @param userEmail The users email
     * @param userPassword The users password
     */
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

    /**
     * Signs out from both the Facebook API and Firebase.
     */
    public static void logOutUser() {
        if(!appData.loggingIn) {
            LoginManager.getInstance().logOut();
        }
        appData.firebaseAuth.signOut();
        appData.currentUser = null;
        Log.d("Appdata", "Logged out user");
    }

    /**
     * Will login to firebase as an anonymous user.
     */
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

    /**
     * When a user has logged in with facebook, this function will use the generated AccessToken to authenticate with firebase.
     * @param token The AccessToken from the facebook API.
     */
    public static void loginFacebook(AccessToken token) {
        Log.d("appData", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        linkWithCredential(credential);
    }

    /**
     * Tries to link an anonymous user with the signed up user, so they will be able to access the same data.
     * If the linking fails the usual login-flow proceeds.
     * @param credential The credentials to login with.
     */
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

    /**
     * Tries to login to firebase with the given credentials. If the current user is anonymous the shoppingcart is saved first so it can be transferred.
     * @param credential The credentials to login with.
     */
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

    /**
     * Saves a users shoppingcart in memory and deletes it from firebase.
     * Proceeds to prepare for login as this is a intermediate step going from anonymous to authenticated user.
     */

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

    /**
     *  Transfers the users saved shoppingcart in memory to the authenticated users shoppingcart in Firebase.
     *  Proceeds to remove the shoppingcart from memory.
     */
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
