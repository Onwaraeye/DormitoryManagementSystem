package com.example.dormitorymanagementsystem.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.dormitorymanagementsystem.ChatNew.ChatActivity;
import com.example.dormitorymanagementsystem.Login;
import com.example.dormitorymanagementsystem.MainActivity;
import com.example.dormitorymanagementsystem.MonthlyBill;
import com.example.dormitorymanagementsystem.Parcel;
import com.example.dormitorymanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FirebaseMessaging extends FirebaseMessagingService {

    private static final String ADMIN_CHANNEL_ID = "admin_channel";


    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences sp = getSharedPreferences("SP_USER", MODE_PRIVATE);
        String saveCurrentUser = sp.getString("Current_USERID", "None");

        String notificationType = remoteMessage.getData().get("notificationType");
        if (notificationType.equals("PostNotification")) {

            String sender = remoteMessage.getData().get("sender");
            String pId = remoteMessage.getData().get("pId");
            String pTitle = remoteMessage.getData().get("pTitle");
            String pDescription = remoteMessage.getData().get("pDescription");

            if (!sender.equals(saveCurrentUser)) {
                showPostNotification("" + pId, "" + pTitle, "" + pDescription);
            }

        } else if (notificationType.equals("ChatNotification")) {

            String sent = remoteMessage.getData().get("sent");
            String user = remoteMessage.getData().get("user");
            String fUser = Login.getGbIdUser();
            if (sent.equals(fUser)) {
                if (!saveCurrentUser.equals(user)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sendOAndAboveNotification(remoteMessage);
                    } else {
                        sendNormalNotification(remoteMessage);
                    }
                }
            }

        }else if (notificationType.equals("ParcelNotification")) {

            String sender = remoteMessage.getData().get("sender");
            String pId = remoteMessage.getData().get("pId");
            String pTitle = remoteMessage.getData().get("pTitle");
            String pDescription = remoteMessage.getData().get("pDescription");

            if (!sender.equals(saveCurrentUser)) {
                showParcelNotification("" + pId, "" + pTitle, "" + pDescription);
            }

        } else if (notificationType.equals("BillNotification")) {

            String pId = remoteMessage.getData().get("pId");
            String pTitle = remoteMessage.getData().get("pTitle");
            String pDescription = remoteMessage.getData().get("pDescription");

            showBillNotification("" + pId, "" + pTitle, "" + pDescription);


        }
    }

    private void showBillNotification(String pId, String pTitle, String pDescription) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = new Random().nextInt(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupBillNotificationChannel(notificationManager);
        }

        Intent intent = new Intent(this, MonthlyBill.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        //PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "" + ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_iconproject2)
                /*.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.iconproject))*/
                .setContentTitle(pTitle)
                .setContentText(pDescription)
                .setContentIntent(pIntent);

        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    private void setupBillNotificationChannel(NotificationManager notificationManager) {
        CharSequence channelName = "New Notification";
        String channelDescription = "Device to device post notification";

        NotificationChannel adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(channelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    private void showParcelNotification(String pId, String pTitle, String pDescription) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = new Random().nextInt(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupParcelNotificationChannel(notificationManager);
        }

        Intent intent = new Intent(this, Parcel.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        //PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "" + ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_iconproject2)
                /*.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.iconproject))*/
                .setContentTitle(pTitle)
                .setContentText(pDescription)
                .setContentIntent(pIntent);

        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    private void setupParcelNotificationChannel(NotificationManager notificationManager) {
        CharSequence channelName = "New Notification";
        String channelDescription = "Device to device post notification";

        NotificationChannel adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(channelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    private void showPostNotification(String pId, String pTitle, String pDescription) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = new Random().nextInt(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupPostNotificationChannel(notificationManager);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("postFromNotification","postFromNotification");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        //PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "" + ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_iconproject2)
                /*.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.iconproject))*/
                .setContentTitle(pTitle)
                .setContentText(pDescription)
                .setContentIntent(pIntent);

        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    private void setupPostNotificationChannel(NotificationManager notificationManager) {
        CharSequence channelName = "New Notification";
        String channelDescription = "Device to device post notification";

        NotificationChannel adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(channelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    private void sendNormalNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("hisUid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_MUTABLE);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_iconproject2)
               /* .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.iconproject))*/
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defSoundUri)
                .setContentIntent(pIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int j = 0;
        if (i > 0) {
            j = i;
        }
        notificationManager.notify(j, builder.build());
    }

    private void sendOAndAboveNotification(RemoteMessage remoteMessage) {
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("hisUid", user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_MUTABLE);
        //PendingIntent pIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoAndAboveNotification notification1 = new OreoAndAboveNotification(this);
        Notification.Builder builder = notification1.getONotifications(title, body, pIntent, defSoundUri, icon);

        int j = 0;
        if (i > 0) {
            j = i;
        }
        notification1.getManager().notify(j, builder.build());
    }

    @Override
    public void onNewToken(@NonNull @NotNull String token) {
        super.onNewToken(token);

        String user = Login.getGbIdUser();
        if (user!=null){
            updateToken(token);
        }

    }

    private void updateToken(String tokenRefresh) {
        String user = Login.getGbIdUser();
        String typeUser = Login.getGbTypeUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token = new Token(tokenRefresh,typeUser);
        ref.child(user).setValue(token);

    }
}
