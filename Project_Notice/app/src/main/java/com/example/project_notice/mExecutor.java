package com.example.project_notice;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.project_notice.Fragment.Fragment1;
import com.example.project_notice.Fragment.Fragment2;
import com.example.project_notice.Fragment.Fragment3;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class mExecutor extends AsyncTask<Void, Void, String> {

        private Elements element;
        private String Title, New_Num, Url, CategoryName;
        private WeakReference<JobService> jobServiceReference;
        private JobParameters params;


    mExecutor(JobService jobService, JobParameters params) {
        jobServiceReference = new WeakReference<>(jobService);
        this.params = params;
    }

    protected String doInBackground(Void... voids) {

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";

        Map<String, String> data = new HashMap<>();
        data.put("pageIndex", String.valueOf(1));
        data.put("cmsNoStr", "");
        data.put("nttId", "");
        data.put("mno", "sub05_01");
        data.put("searchCondition", "all");
        data.put("searchKeyword", "");
        data.put("pageUnit", "10");

        try
        {
            Document doc = Jsoup.connect(jobScheduler.Url)
                    .data(data)
                    .userAgent(userAgent)
                    .timeout(3000)
                    .post();

            int NoticeNum = doc.select("#txt > div > div.no-more-tables > table > tbody > tr.notice").size() + 1;
                element = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + NoticeNum + ") > td:nth-child(1)");
                New_Num = element.text();

                element = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + NoticeNum + ") > td:nth-child(2)");
                Title = element.text();

                  Element link = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + NoticeNum + ") > td:nth-child(2)").first();
                   String linkInnerH = link.html().split("'")[1];
                   Url= (jobScheduler.Url+ linkInnerH).replaceAll("list.do","view.do?nttId=");;

                element = doc.select("head > meta:nth-child(5)");
                CategoryName = element.text();

        }
        catch (Exception e)
        {
            return null;
        }

        return "주소: " +  Url + " 제목:" + Title + " 이후:" + New_Num;
    }

}
