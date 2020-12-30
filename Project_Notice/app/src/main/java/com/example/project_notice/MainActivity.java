package com.example.project_notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_notice.Fragment.Fragment1;
import com.example.project_notice.Fragment.Fragment2;
import com.example.project_notice.Fragment.Fragment3;
import com.example.project_notice.Fragment.Fragment4;
import com.example.project_notice.Fragment.Fragment5;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    BottomNavigationView bottomNavigation;
    Fragment1 fragment1; Fragment2 fragment2; Fragment3 fragment3; Fragment4 fragment4; Fragment5 fragment5;
    public static ArrayList<DataList> Blist = new ArrayList<>();

    public static Context c;

    public MainActivity(){
        this.c = this;
    }

    private static final int job_code = 20187097;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_main);

        setTheme(android.R.style.Theme_NoTitleBar);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        fragment5 = new Fragment5();

        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment4).commitAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment1).commitAllowingStateLoss();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab1:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment1).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tab2:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment2).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tab3:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment3).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tab4:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment4).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tab5:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment5).commitAllowingStateLoss();
                        return true;
                    }
                    default: return false;
                }
            }
        });
    }

    public void initJobScheduler(){
        try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ComponentName componentName = new ComponentName(this, jobScheduler.class);
            PersistableBundle bundle = new PersistableBundle();
            JobInfo.Builder builder = new JobInfo.Builder(job_code,componentName)
                    .setExtras(bundle)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true);

          /*  if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N){
                builder.setPeriodic(15 * 60 * 1000, 15 * 60 * 1000);
            } else {
                builder.setPeriodic(15 * 60 * 1000);
            }*/

           if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N){
                builder.setPeriodic(Fragment3.TimeVal,Fragment3.TimeVal);
            } else {
                builder.setPeriodic(Fragment3.TimeVal);
            }

            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        }
        } catch (Exception e){ }
    }

    public void scheduljob(){
        Log.d("Tag","스케쥴러 실행");
        initJobScheduler();
    }

    public void clearjob(){
        Log.d("Tag","스케쥴러 취소");
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(job_code);
    }

    public void SetNavState(int id) {
        BottomNavigationView nav = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(id);
    }

    @Override
    public void onClick(View v) {}

    public void bListinput(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.Blist);
        editor.putString("blist",json);
        editor.apply();
    }

    public void LoadData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("blist",null);
        Type type = new TypeToken<ArrayList<DataList>>() {}.getType();
        MainActivity.Blist = gson.fromJson(json,type);

    }

}
