package com.example.dvcbaberbooking.Common;

import android.content.Context;
import android.text.TextUtils;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.NotificationCompat;

import com.example.dvcbaberbooking.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;

import com.example.dvcbaberbooking.Model.MyToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import io.paperdb.Paper;

public class Common {
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    //    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
//    public static final String KEY_SALON_STORE = "SALON_SAVE";
//    public static final String KEY_BARBER_LOAD_DONE = "BARBER_LOAD_DONE";
//    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
//    public static final String KEY_STEP = "STEP";
//    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";
//    public static final int TIME_SLOT_TOTAL = 20;
//    public static final Object DISABLE_TAG = "DISABLE";
//    public static final String KEY_TIME_SLOT = "TIME_SLOT";
//    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
//    public static final String EVENT_URI_CACHE = "URI_EVENT_SAVE";
//    public static final String TITLE_KEY = "title";
//    public static final String CONTENT_KEY = "content";
    public static final String LOGGED_KEY = "UserLogged";
//    public static final String RATING_INFORMATION_KEY = "RATING_INFORMATION";
//
//    public static final String RATING_STATE_KEY = "RATING_STATE";
//    public static final String RATING_SALOON_ID = "RATING_SALOON_ID";
//    public static final String RATING_SALOON_NAME = "RATING_SALOON_NAME";
//    public static final String RATING_BARBER_ID = "RATING_BARBER_ID";


//    public static String convertTimeStampToStringKey(Timestamp timestamp) {
//        Date date = timestamp.toDate();
//        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd_MM_yyyy");
//        return simpleDateFormat1.format(date);
//    }

//    public static String formatShoppingItemName(String name) {
//        return name.length() > 13 ? new StringBuilder(name.substring(0, 10)).append("...").toString() : name;
//    }

//    public static void showNotification(Context context, int noti_id, String title, String content, Intent intent) {
//        PendingIntent pendingIntent = null;
//        if (intent != null)
//            pendingIntent = PendingIntent.getActivity(context,
//                    noti_id,
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//        String NOTIFICATION_CHANNEL_ID = "docbooking_staff_channel_01";
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
//                    "Doc Booking Staff App", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationChannel.setDescription("Staff App");
//            notificationChannel.enableLights(true);
//            notificationChannel.enableVibration(true);
//
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
//
//        builder.setContentTitle(title)
//                .setContentText(content)
//                .setAutoCancel(false)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
//
//
//        if (pendingIntent != null)
//            builder.setContentIntent(pendingIntent);
//
//        Notification notification = builder.build();
//
//        notificationManager.notify(noti_id, notification);
//    }


    public static enum TOKEN_TYPE {
        CLIENT,
        BARBER,
        MANAGER

    }
    public static void updateToken(Context context, final String s) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if ( user!=null){


            MyToken myToken = new MyToken();
            myToken.setToken(s);
            myToken.setTokenType(TOKEN_TYPE.CLIENT);
            myToken.setUserPhone(user.getPhoneNumber());

            FirebaseFirestore.getInstance()
                    .collection("User")
                    .document(user.getPhoneNumber())
                    .set(myToken)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });


        }else {
            Paper.init(context);
            String locaUser = Paper.book().read(Common.LOGGED_KEY);
            if (locaUser != null) {
                if (!TextUtils.isEmpty(locaUser)) {
                    MyToken myToken = new MyToken();
                    myToken.setToken(s);
                    myToken.setTokenType(TOKEN_TYPE.CLIENT);
                    myToken.setUserPhone(locaUser);
//Tokens
                    FirebaseFirestore.getInstance()
                            .collection("User")
                            .document(locaUser)
                            .set(myToken)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
            }

        }
    }
}


