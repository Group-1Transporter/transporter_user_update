package com.transporteruser;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class ReceivingPushNotification extends FirebaseMessagingService {
    String currentUserId;
    Intent intent;
    PendingIntent pendingIntent;
    @Override
    public void onNewToken(String token) {
        updateToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            Map<String, String> map = remoteMessage.getData();
            String title = map.get("title");
            String description = map.get("body");
            intent = new Intent(this,MainActivity.class);
            pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String channelId = "Test channel";
            String channelName = "Test";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder nb = new NotificationCompat.Builder(this, channelId);

            nb.setContentTitle(title);
            nb.setContentText(description);
            nb.setContentIntent(pendingIntent);
            nb.setSmallIcon(R.drawable.applogo);
            manager.notify(1, nb.build());
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateToken(final String token) {
        currentUserId = FirebaseAuth.getInstance().getUid();
        final UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.checkProfile(currentUserId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200) {
                    User user = response.body();
                    user.setToken(token);
                    userApi.updateProfile(user).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.code() == 200){

                            }
                            
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(ReceivingPushNotification.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ReceivingPushNotification.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
