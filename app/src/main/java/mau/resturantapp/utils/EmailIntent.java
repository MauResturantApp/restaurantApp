package mau.resturantapp.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

public class EmailIntent {

    /**
     * Will open and intent with the user's preferred email client and have the user send email manually.
     * Multiple addresses can be given with the first parameter, "address".
     *
     * @param address Receiving email address(es)
     * @param subject Subject of email
     * @param body Contents of the email
     */
    public static Intent sendEmail(String[] address, String subject, String body) {
        Intent i = new Intent(Intent.ACTION_SENDTO);

        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, address);
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, body);

        return i;
    }
}
