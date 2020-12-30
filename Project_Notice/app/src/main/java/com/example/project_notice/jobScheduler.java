package com.example.project_notice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Switch;

import androidx.core.app.NotificationCompat;

import com.example.project_notice.Fragment.Fragment1;
import com.example.project_notice.Fragment.Fragment3;

public class jobScheduler extends JobService {

    private mExecutor mExecutor;
    public static Context c;
    public static String Url, CategoryName;
    private String Pre_Num, New_Num, Title, connect_url;

    public void onCreate() {
        super.onCreate();

        if (Url!="" && Url != null){
            SharedPreferences.Editor editor = getSharedPreferences("Work_URL", MODE_PRIVATE).edit();
            editor.putString("Work_URL",Url);
            editor = getSharedPreferences("Work_Category", MODE_PRIVATE).edit();

            editor.putString("Work_Category",CategoryName);
            editor.commit();
        } else {
            SharedPreferences prefs = getSharedPreferences("Work_URL", MODE_PRIVATE);
            Url = prefs.getString("Work_URL", "0");

            prefs = getSharedPreferences("Work_Category", MODE_PRIVATE);
            CategoryName = prefs.getString("Work_Category", "0");
        }
    }

    @Override
    public boolean onStartJob(final JobParameters params) {

        c= this;

        mExecutor = new mExecutor(this,params){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                SharedPreferences.Editor editor = getSharedPreferences("Work_Num", MODE_PRIVATE).edit();
                SharedPreferences prefs = getSharedPreferences("Work_Num", MODE_PRIVATE);

                New_Num = s.split("이후:")[1];
                Title = s.split("제목:")[1].split(" 이후")[0];
                Pre_Num = prefs.getString("Work_Num", "0");
                connect_url = s.split("주소: ")[1].split(" 제목:")[0];

                if (!New_Num.equals(Pre_Num)){
                    getNotification(c,Title,connect_url);
                    editor.putString("Work_Num", New_Num);
                    editor.commit();
                }

                jobFinished(params, true);
            }
        };

        mExecutor.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mExecutor.cancel(true);
        return false;
    }

    public void getNotification(Context c, String Title, String connect_url){
        Context context = c;

        NotificationManager manager;
        NotificationCompat.Builder builder;
        String CHANNEL_ID = "20187097";
        String CHANEL_NAME = "변형민";

        manager = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new NotificationCompat.Builder(c,CHANNEL_ID);
        } else{
            builder = new NotificationCompat.Builder(c);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(connect_url));
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle("[" + CategoryName + "] " + "새로운 공지글이 등록되었습니다.");
        builder.setContentText(Title);
        builder.setSmallIcon(R.drawable.notification_48px);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        manager.notify(1,notification);
    }

}
