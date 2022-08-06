package com.example.dormitorymanagementsystem.notifications;

import com.example.dormitorymanagementsystem.Login;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseService {

    public void onTokenRefresh(){

        String getUid = Login.getGbIdUser();
        updateToken(getUid);

    }

    private void updateToken(String tokenRefresh) {

        String getUid = Login.getGbIdUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tokenRefresh);
        ref.child(getUid).setValue(token);

    }
}
