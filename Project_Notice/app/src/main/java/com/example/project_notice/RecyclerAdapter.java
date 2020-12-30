package com.example.project_notice;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_notice.Fragment.Fragment2;
import com.example.project_notice.Fragment.Fragment4;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>  {

    private Context context;
    public ArrayList<DataList> listData = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnTitemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter.ItemViewHolder itemViewHolder, final int i) {

        itemViewHolder.onBind(listData.get(i));
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(listData.get(i).getUrl()));
                context.startActivity(intent);
            }
        });

        itemViewHolder.bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listData.get(i).getBooked() != Integer.parseInt(String.valueOf(R.drawable.bookmark_24px))) {
                    listData.get(i).setChecked(false);
                    listData.get(i).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark_24px)));
                } else {
                    listData.get(i).setChecked(true);
                    listData.get(i).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark1_24px)));
                }

                if (mListener != null) mListener.onItemClick(v, i);
                notifyItemChanged(i);
            }
        });
    }

    public void clear() {
        int size = listData.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                listData.remove(0);
            }
            notifyItemRangeRemoved(0,size);
        }
    }

    @Override
    public int getItemCount() { return listData.size(); }

    public void addItem(DataList data) { listData.add(data); }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView ArticleTitle, ArticleDate, ArticleFileName;
        private ImageView img_chart;
        private RelativeLayout BackColor;
        private Button bookMark;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ArticleTitle = itemView.findViewById(R.id.ArticleTitle);
            ArticleDate = itemView.findViewById(R.id.ArticleDate);
            ArticleFileName = itemView.findViewById(R.id.FileName);
            BackColor = itemView.findViewById(R.id.backRow);
            bookMark = itemView.findViewById(R.id.BookMark);
            img_chart = itemView.findViewById(R.id.CardImg);
        }

        void onBind(DataList data){
            ArticleTitle.setText(data.getArticleTitle());
            ArticleDate.setText(data.getArticleDate());
            ArticleFileName.setText(data.getFileNAME());
            BackColor.setBackgroundColor(data.getColor());
            bookMark.setBackgroundResource(data.getBooked());
            img_chart.setImageResource(data.getImageUrl());
        }
    }

}
