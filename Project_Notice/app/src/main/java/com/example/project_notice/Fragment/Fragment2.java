package com.example.project_notice.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_notice.DataList;
import com.example.project_notice.MainActivity;
import com.example.project_notice.R;
import com.example.project_notice.RecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// 공지글리스트를 보여줌

public class Fragment2 extends Fragment{

    ViewGroup viewGroup;
    RecyclerView ArticleList;
    RecyclerAdapter myAdapter;
    private ImageView Nosearch;

    TextView Page, ArticleNms,CategoryName,Search_result;

    public static String Search_Url = "";
    public static boolean End_Page;

    private String Category, searchKeyword = null, ViewType = "", preUrl = "";
    public int PageIdx = 1, NoticeNum;

   public ArrayList<DataList> noticeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment2,container,false);
        CategoryName = (TextView)viewGroup.findViewById(R.id.CategoryName);
        Page = (TextView) viewGroup.findViewById(R.id.Page);
        ArticleNms = (TextView) viewGroup.findViewById(R.id.ArticleNums);
        Nosearch = (ImageView) viewGroup.findViewById(R.id.No_search);
        Search_result = (TextView) viewGroup.findViewById(R.id.Search_result);
        final FloatingActionButton ftBtn = (FloatingActionButton) viewGroup.findViewById(R.id.fab_main);

        ftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleList.scrollToPosition(0);
            }
        });

        if (preUrl != Search_Url)  {
            ViewType = "공지"; PageIdx = 1; End_Page = false;
            preUrl = Search_Url; noticeList.clear();
        }

        if (Search_Url.contains("BBSMSTR_000000000333"))
            Category = "컴퓨터공학과";
        else if (Search_Url.contains("BBSMSTR_000000000340"))
            Category = "전자제어공학과";
        else if (Search_Url.contains("BBSMSTR_000000000348"))
            Category = "전기공학과";
        else if (Search_Url.contains("BBSMSTR_000000000323"))
            Category = "정보통신공학과";
        else if (Search_Url.contains("BBSMSTR_000000000050"))
            Category = "전체공지";
        else Category = "";

        CategoryName.setText(Category);

        ArticleList = (RecyclerView) viewGroup.findViewById(R.id.ArticleList);
        ArticleList.setLayoutManager(new LinearLayoutManager(getActivity()));

        myAdapter = new RecyclerAdapter(getActivity());
        ArticleList.setAdapter(myAdapter);
        ArticleList.scrollToPosition(0);
        ArticleList.setItemAnimator(new DefaultItemAnimator());

        SetData(0);
        removeBook();

        if (myAdapter.getItemCount() < 1) if(Category != "") new MainPageTask().execute();
        Refresh_View();

        if (Search_Url != "") {
            Page.setText("페이지 : " + String.valueOf(CurrentPage()));
            ArticleNms.setText("게시물 : " + String.valueOf(myAdapter.getItemCount()));
        }

        if (myAdapter.getItemCount() < 1 ) Nosearch.setVisibility(viewGroup.VISIBLE); else Nosearch.setVisibility(viewGroup.INVISIBLE);
        if (myAdapter.getItemCount() < 1 ) Search_result.setVisibility(viewGroup.VISIBLE); else Search_result.setVisibility(viewGroup.INVISIBLE);

        myAdapter.setOnTitemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if (noticeList.get(pos).getBooked() == Integer.parseInt(String.valueOf(R.drawable.bookmark_24px))) {
                    noticeList.get(pos).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark1_24px)));
                    DataList d = new DataList(noticeList.get(pos).getArticleTitle(),
                            noticeList.get(pos).getArticleDate(),
                            noticeList.get(pos).getUrl(),
                            noticeList.get(pos).getImageUrl(),
                            noticeList.get(pos).getColor(),
                            noticeList.get(pos).getBooked());

                    MainActivity.Blist.add(d);
                } else {
                    noticeList.get(pos).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark_24px)));
                    removeBooked(noticeList.get(pos).getArticleTitle());
                }
                myAdapter.notifyItemChanged(pos);
            }
        });

        Button btnSearch = (Button) viewGroup.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewType = "검색";
                myAdapter.clear(); End_Page = false; PageIdx =1; noticeList.clear();
                EditText edtSearch = viewGroup.findViewById(R.id.edtSearch);
                searchKeyword = edtSearch.getText().toString();

                if (searchKeyword.equals("")) {ViewType = "공지"; Refresh_View();}
                TextView Keyword = viewGroup.findViewById(R.id.Keyword);
                Keyword.setText("[" + searchKeyword + "]");
                edtSearch.setText("");

                if (!End_Page) new SearchTask().execute();

            }
        });
        ArticleList.addOnScrollListener(onScrollListener);
        return viewGroup;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
            int totalItemCount = layoutManager.getItemCount();
            int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

            if ((lastVisible >= totalItemCount - 1)) {
                if (ViewType.equals("공지")) {
                    if (!End_Page) new MainPageTask().execute();
                } else {
                    if (!End_Page) new SearchTask().execute();
                }
            }
        }
    };

    //공지사항의 즐겨찾기 제거
    private void removeBook(){

        for (int j=0; j < noticeList.size(); j ++) {
            if (MainActivity.Blist.size() > 0) {
                for (int i = 0; i < MainActivity.Blist.size(); i++) {
                    if (MainActivity.Blist.get(i).getBooked() == Integer.parseInt(String.valueOf(R.drawable.bookmark_24px))) {
                        if (noticeList.get(j).getArticleTitle().contains(MainActivity.Blist.get(i).getArticleTitle())) {
                            noticeList.get(j).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark_24px)));
                        }
                    }
                }
            } else {
                noticeList.get(j).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark_24px)));
          }
            myAdapter.clear();
            SetData(0);
            myAdapter.notifyDataSetChanged();
        }
    }

    //북마크의 즐겨찾기 제거
    private void removeBooked(String val){
            for (int i=0; i < MainActivity.Blist.size(); i++){
                if (val.contains(MainActivity.Blist.get(i).getArticleTitle()))
                    MainActivity.Blist.get(i).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark_24px)));
            }
    }

    private void getBooked(){

        for (int i = 0; i < noticeList.size(); i ++){
            for (int j = 0; j < MainActivity.Blist.size(); j ++){
                if(noticeList.get(i).getArticleTitle().contains(MainActivity.Blist.get(j).getArticleTitle()))
                    noticeList.get(i).setBooked(Integer.parseInt(String.valueOf(R.drawable.bookmark1_24px)));
                myAdapter.clear();
                SetData(0);
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    private int CurrentPage(){
        int CurrentPage = (noticeList.size()-NoticeNum) / 10;
        if (noticeList.size() % 10 > 0) CurrentPage +=1;
        if (CurrentPage <= 0) CurrentPage = 1;
        return CurrentPage;
    }

    private void Refresh_View() { myAdapter.notifyDataSetChanged(); }

    public void SetData(int value){
        for (int q = value; q < noticeList.size(); q++) {
            DataList d = new DataList(noticeList.get(q).getArticleTitle(),
                                      noticeList.get(q).getArticleDate(),
                                      noticeList.get(q).getUrl(),
                                      noticeList.get(q).getImageUrl(),
                                      noticeList.get(q).getColor(),
                                      noticeList.get(q).getBooked());
            myAdapter.addItem(d);
        }

    }

    public class MainPageTask extends AsyncTask<Void, Void, String> {

        private Elements element;
        private ProgressDialog progressDialog;

        private int i; int ArticleNum; private int Items_count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ViewType = "공지";

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("공지글 목록을 불러오는 중입니다.");
            progressDialog.show();

            Refresh_View();

        }

        @Override
        protected void onPostExecute(String result) {

            getBooked();

            if (ArticleNum < 10) {
                Toast.makeText(getActivity(),"마지막 페이지 입니다.",Toast.LENGTH_SHORT).show();
                End_Page = true;
                PageIdx = 10000;
            } else PageIdx += 1;

            Page = (TextView)viewGroup.findViewById(R.id.Page);
            ArticleNms = (TextView)viewGroup.findViewById(R.id.ArticleNums);

            Page.setText("페이지 : " +String.valueOf(CurrentPage()));
            ArticleNms.setText("게시물 : " + String.valueOf(myAdapter.getItemCount()));

            Refresh_View();

            if (myAdapter.getItemCount() < 1 ) Nosearch.setVisibility(viewGroup.VISIBLE); else Nosearch.setVisibility(viewGroup.INVISIBLE);
            if (myAdapter.getItemCount() < 1 ) Search_result.setVisibility(viewGroup.VISIBLE); else Search_result.setVisibility(viewGroup.INVISIBLE);

            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... params) {

            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";

            Map<String, String> data = new HashMap<>();
            data.put("pageIndex", String.valueOf(PageIdx));
            data.put("cmsNoStr", "");
            data.put("nttId", "");
            data.put("mno", "sub05_01");
            data.put("searchCondition", "all");
            data.put("searchKeyword", "");
            data.put("pageUnit", "10");

            try
            {
                Document doc = Jsoup.connect(Search_Url)
                        .data(data)
                        .userAgent(userAgent)
                        .timeout(3000)
                        .post();

                ArticleNum = doc.select("#txt > div > div.no-more-tables > table > tbody > tr").size();
                NoticeNum = doc.select("#txt > div > div.no-more-tables > table > tbody > tr.notice").size();
                if (PageIdx > 1)
                    i = NoticeNum + 1;
                else
                    i = 1;

                Items_count = noticeList.size();

                String title, date, url;
                int bookmark, nImg, backColor;
                for (; i <= ArticleNum; i ++) {
                    element = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td.subject");
                    title = element.text();

                    element = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td.regDate");
                    date = element.text();


                    Element link = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td:nth-child(2)").first();
                    String linkInnerH = link.html().split("'")[1];
                    url = Search_Url.replaceAll("/list.do","").replaceAll("mno=sub07_01","")+"/view.do?nttId=" + linkInnerH;

                    Element img = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td:nth-child(1)").first();
                    String imgHtml = img.html();

                    bookmark = Integer.parseInt(String.valueOf(R.drawable.bookmark_24px));
                    int State_New =  doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td.subject > span").size();

                    if (imgHtml.contains("공지")){
                        if (State_New == 1)
                            nImg = Integer.parseInt(String.valueOf(R.drawable.new_24px));
                        else
                            nImg = Integer.parseInt(String.valueOf(R.drawable.white));

                        backColor = Color.rgb(252,253,231);
                    }
                    else if (State_New == 1){
                        nImg = Integer.parseInt(String.valueOf(R.drawable.new_24px));
                        backColor = Color.WHITE;
                    } else{
                        nImg = Integer.parseInt(String.valueOf(R.drawable.white));
                        backColor = Color.WHITE;
                    }

                    noticeList.add(new DataList(title, date, url,  nImg, backColor, bookmark));

                }
                SetData(Items_count);
            }

            catch (Exception e)
            {
                return null;
            }
            return null;
        }
    }

    public class SearchTask extends AsyncTask<Void, Void, String> {

        private int i = 1; private int ArticleNum; private int Items_count;

        private Elements element;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ViewType = "검색";

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("공지글 목록을 불러오는 중입니다.");
            progressDialog.show();

            Refresh_View();

        }

        @Override
        protected void onPostExecute(String result) {

            getBooked();

            if (ArticleNum < 10) {
                End_Page = true;
                PageIdx = 10000;
                Toast.makeText(getActivity(),"마지막 페이지 입니다.",Toast.LENGTH_SHORT).show();
            } else PageIdx += 1;

            Page = (TextView)viewGroup.findViewById(R.id.Page);
            ArticleNms = (TextView)viewGroup.findViewById(R.id.ArticleNums);

            Page.setText("페이지 : " +String.valueOf(CurrentPage()));
            ArticleNms.setText("게시물 : " + String.valueOf(myAdapter.getItemCount()));

            Refresh_View();

            progressDialog.dismiss();

            if (myAdapter.getItemCount() < 1 ) Nosearch.setVisibility(viewGroup.VISIBLE); else Nosearch.setVisibility(viewGroup.INVISIBLE);
            if (myAdapter.getItemCount() < 1 ) Search_result.setVisibility(viewGroup.VISIBLE); else Search_result.setVisibility(viewGroup.INVISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {

            ViewType = "검색";

            String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";

            Map<String, String> data = new HashMap<>();
            data.put("pageIndex", String.valueOf(PageIdx));
            data.put("cmsNoStr", "");
            data.put("nttId", "");
            data.put("mno", "");
            data.put("searchCondition", "all");
            data.put("searchKeyword", searchKeyword);
            data.put("pageUnit", "10");

            Log.d("PageIdx",searchKeyword + PageIdx);

            try
            {
                Document doc = Jsoup.connect(Search_Url)
                        .data(data)
                        .userAgent(userAgent)
                        .timeout(3000)
                        .post();

                ArticleNum = doc.select("#txt > div > div.no-more-tables > table > tbody > tr").size();
                NoticeNum = doc.select("#txt > div > div.no-more-tables > table > tbody > tr.notice").size();
                if (PageIdx > 1)
                    i = NoticeNum + 1;
                else
                    i = 1;

                Items_count = noticeList.size();

                String title, date, url;
                int bookmark, nImg, backColor;
                for (; i <= ArticleNum; i ++) {
                    element = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td.subject");
                    title = element.text();

                    element = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td.regDate");
                    date = element.text();


                    Element link = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td:nth-child(2)").first();
                    String linkInnerH = link.html().split("'")[1];
                    url = Search_Url.replaceAll("/list.do","").replaceAll("mno=sub07_01","")+"/view.do?nttId=" + linkInnerH;

                    Element img = doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td:nth-child(1)").first();
                    String imgHtml = img.html();

                    bookmark = Integer.parseInt(String.valueOf(R.drawable.bookmark_24px));
                    int State_New =  doc.select("#txt > div > div.no-more-tables > table > tbody > tr:nth-child(" + i + ") > td.subject > span").size();

                    if (imgHtml.contains("공지")){
                        if (State_New == 1)
                            nImg = Integer.parseInt(String.valueOf(R.drawable.new_24px));
                        else
                            nImg = Integer.parseInt(String.valueOf(R.drawable.white));

                        backColor = Color.rgb(252,253,231);
                    }
                    else if (State_New == 1){
                        nImg = Integer.parseInt(String.valueOf(R.drawable.new_24px));
                        backColor = Color.WHITE;
                    } else{
                        nImg = Integer.parseInt(String.valueOf(R.drawable.white));
                        backColor = Color.WHITE;
                    }

                    noticeList.add(new DataList(title, date, url,  nImg, backColor, bookmark));

                }
                SetData(Items_count);
            }

            catch (Exception e)
            {
                return null;
            }
            return null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

}