package com.example.firebaseteste;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //private String token;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

   /* public String getToken() {
        return token;
    }*/

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //token = s;
        //Log.i("onNewToken","onNewToken: " + s);
    }
}
