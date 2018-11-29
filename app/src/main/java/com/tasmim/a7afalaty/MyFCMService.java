package com.tasmim.a7afalaty;

import android.util.Log;

import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;


/**
 * Created by Ma7MouD on 4/30/2018.
 */

public class MyFCMService extends FirebaseMessagingService {

    int count = 0;
    int notification_id = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            Badges.setBadge(this, ++count);
        } catch (BadgesNotSupportedException badgesNotSupportedException) {
            Log.d(TAG, badgesNotSupportedException.getMessage());
        }

//        String Ntitle = remoteMessage.getData().get("title");
//        String Nmessage = remoteMessage.getData().get("message");

        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().get("title").equals("حجز جديد")) {
                String Ntitle = remoteMessage.getData().get("title");
                String Nmessage = remoteMessage.getData().get("body");
//                String client_id = remoteMessage.getData().get("client_id");
//                String client_name = remoteMessage.getData().get("client_name");
//                String place_name = remoteMessage.getData().get("place_name");
//                String address = remoteMessage.getData().get("address");
//                String desc = remoteMessage.getData().get("order_desc");
//                String deliver_date = remoteMessage.getData().get("deliver_date");
//                String order_id = remoteMessage.getData().get("order_id");

                NotificationHelper.getInstance(this).createNotification(Ntitle, Nmessage);
            } else if (remoteMessage.getData().get("title").equals("تعليق جديد")) {
                String Ntitle = remoteMessage.getData().get("title");
//                String type = remoteMessage.getData().get("type");
                String Nmessage = remoteMessage.getData().get("body");
//                String client_id = remoteMessage.getData().get("client_id");
//                String client_name = remoteMessage.getData().get("client_name");
//                String place_name = remoteMessage.getData().get("place_name");
//                String address = remoteMessage.getData().get("address");
//                String desc = remoteMessage.getData().get("order_desc");
//                String deliver_date = remoteMessage.getData().get("deliver_date");
//                String order_id = remoteMessage.getData().get("order_id");

                NotificationHelper.getInstance(this).createNotification(Ntitle, Nmessage);
            }else if (remoteMessage.getData().get("title").equals("قبول الحجز")) {
                String Ntitle = remoteMessage.getData().get("title");
//                String type = remoteMessage.getData().get("type");
                String Nmessage = remoteMessage.getData().get("body");
//                String client_id = remoteMessage.getData().get("client_id");
//                String client_name = remoteMessage.getData().get("client_name");
//                String place_name = remoteMessage.getData().get("place_name");
//                String address = remoteMessage.getData().get("address");
//                String desc = remoteMessage.getData().get("order_desc");
//                String deliver_date = remoteMessage.getData().get("deliver_date");
//                String order_id = remoteMessage.getData().get("order_id");

                NotificationHelper.getInstance(this).createNotification(Ntitle, Nmessage);
            }

        }
    }
}