package com.example.project_notice.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project_notice.MainActivity;
import com.example.project_notice.R;
import com.example.project_notice.jobScheduler;


// 어플의 설정을 관리

public class Fragment3 extends Fragment {

    TextView NameText;
    ViewGroup viewGroup;
    Switch Switch;
    Spinner spinner;
    Button bookBtn;

    public static long TimeVal = 15*60*1000;

    public static boolean Alert_chk; String Category_URL, CategoryName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
         viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment3,container,false);
         NameText = (TextView) viewGroup.findViewById(R.id.NameText);
         Switch = (Switch) viewGroup.findViewById(R.id.Switch);
         spinner = (Spinner) viewGroup.findViewById(R.id.Spinner1);
         bookBtn = (Button) viewGroup.findViewById(R.id.bookBtn);

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"북마크 목록이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).Blist.clear();
            }
        });

        ((MainActivity) getActivity()).clearjob();

        SharedPreferences appData = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        Switch.setChecked(appData.getBoolean("Alert_chk", false));
        NameText.setText(appData.getString("CategoryName", Fragment1.NameStr));

        if (Fragment1.NameStr == "관심 카테고리를 선택해주세요") Fragment1.NameStr = appData.getString("CategoryName", Fragment1.NameStr);
        if (Fragment1.Alert_Url == "") Fragment1.Alert_Url = appData.getString("Category_URL", Fragment1.Alert_Url);
        NameText.setText(Fragment1.NameStr);

        CategoryName = Fragment1.NameStr;
        Category_URL = Fragment1.Alert_Url;

        jobScheduler.Url = Fragment1.Alert_Url;
        jobScheduler.CategoryName = CategoryName;


        if (Switch.isChecked() || Fragment1.NameStr != "관심 카테고리를 선택해주세요") ((MainActivity) getActivity()).scheduljob(); else ((MainActivity) getActivity()).clearjob();
        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Alert_chk = Switch.isChecked();
                if (!Alert_chk) spinner.setEnabled(false); else spinner.setEnabled(true);
            }
        });
        Alert_chk = Switch.isChecked();

        if (!Alert_chk) spinner.setEnabled(false); else spinner.setEnabled(true);

        final String[] TimeSpinner = getResources().getStringArray(R.array.TimeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.TimeSpinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (TimeSpinner[position]){
                    case "30분":
                        TimeVal= 1800;
                        break;
                    case "1시간":
                        TimeVal= 3600;
                        break;
                    case "2시간":
                        TimeVal= 5400;
                        break;

                    case "4시간":
                        TimeVal= 10800;
                        break;

                    case "8시간":
                        TimeVal= 21600;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(adapter);

    return viewGroup;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedpreferences;
        sharedpreferences = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putBoolean("Alert_chk",Alert_chk);
        editor.putString("CategoryName",CategoryName);
        editor.putString("Category_URL",Fragment1.Alert_Url);

        editor.commit();

        if (Alert_chk)
            ((MainActivity) getActivity()).scheduljob();
        else
            ((MainActivity) getActivity()).clearjob();

    }
}

