package com.example.project_notice.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_notice.DataList;
import com.example.project_notice.MainActivity;
import com.example.project_notice.R;
import com.example.project_notice.RecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Fragment4 extends Fragment{

    ViewGroup viewGroup;
    RecyclerAdapter myAdapter;
    RecyclerView BookList;

    private boolean loadBlist = false;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment4,container,false);

        ImageView No_search = viewGroup.findViewById(R.id.No_search);
        BookList = (RecyclerView) viewGroup.findViewById(R.id.BookList);
        TextView ArticleNums = viewGroup.findViewById(R.id.ArticleNums);

        if (MainActivity.Blist == null) MainActivity.Blist = new ArrayList<>();

        BookList.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new RecyclerAdapter(getActivity());
        BookList.scrollToPosition(0);
        BookList.setItemAnimator(new DefaultItemAnimator());
        BookList.setAdapter(myAdapter);

        removeBook();

        if(!loadBlist) { loadBlist = true; ((MainActivity)getActivity()).LoadData(getActivity()); }

        myAdapter.clear();
        SetData(0);
        Refresh_View();

        ArticleNums.setText("북마크 수: " + myAdapter.getItemCount() + "개");

        if (myAdapter.getItemCount() < 1 )
            No_search.setVisibility(View.VISIBLE);
        else
            No_search.setVisibility(View.INVISIBLE);


        // 어댑터 클릭시 북마크 표시 및 상태변환
        myAdapter.setOnTitemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (String.valueOf(MainActivity.Blist.get(pos).getBooked()).equals(String.valueOf(R.drawable.bookmark_24px))) {
                    MainActivity.Blist.get(pos).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark1_24px)));
                    MainActivity.Blist.get(pos).setdeleteNum(0);
                } else {
                    MainActivity.Blist.get(pos).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark_24px)));
                    MainActivity.Blist.get(pos).setdeleteNum(MainActivity.Blist.get(pos).getdeleteNum());
                }
                myAdapter.notifyItemChanged(pos);
            }
        });

        return viewGroup;
    }


    private void Refresh_View() { myAdapter.notifyDataSetChanged(); }

    public void SetData(int value){
        try {
            for (int q = value; q < MainActivity.Blist.size(); q++) {
                DataList d = new DataList(MainActivity.Blist.get(q).getArticleTitle(),
                                          MainActivity.Blist.get(q).getArticleDate(),
                                          MainActivity.Blist.get(q).getUrl(),
                                          MainActivity.Blist.get(q).getBooked());
                myAdapter.addItem(d);
            }
        }
        catch(Exception e){
        }
    }

    public void removeBook(){
        for (int i =MainActivity.Blist.size()-1; i > -1; i--){
            if (MainActivity.Blist.get(i).getBooked() != Integer.parseInt(String.valueOf(R.drawable.bookmark1_24px)))
            MainActivity.Blist.remove(i);
        }
        myAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).bListinput(getActivity());
    }

}
