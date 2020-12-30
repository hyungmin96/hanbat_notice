package com.example.project_notice.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.project_notice.MainActivity;
import com.example.project_notice.R;


// 공지글을 확인할 카테고리(과)를 설정

public class Fragment1 extends Fragment{
    ViewGroup viewGroup;
    static String NameStr = "관심 카테고리를 선택해주세요";
    public static String Alert_Url="";
    @Nullable @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1,container,false);

        //컴퓨터공학과
        LinearLayout LN = (LinearLayout) viewGroup.findViewById(R.id.linearLayout);
        LN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment2();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_layout, fragment).commit();

                ((MainActivity) getActivity()).SetNavState(R.id.tab2);
                Fragment2.Search_Url = "https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000333/list.do";
            }
        });

        LN.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                 NameStr = "컴퓨터공학과";
                 Alert_Url = "https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000333/list.do";
                Toast.makeText(container.getContext(),"'" + NameStr + "' 가 카테고리로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //전자제어공학과
        LinearLayout LN4 = (LinearLayout) viewGroup.findViewById(R.id.linearLayout4);
        LN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment2();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_layout, fragment).commit();

                ((MainActivity) getActivity()).SetNavState(R.id.tab2);
                Fragment2.Search_Url = "https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000340/list.do";
            }
        });
        LN4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NameStr = "전자제어공학과";
                Alert_Url ="https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000340/list.do";
                Toast.makeText(container.getContext(),"'" + NameStr + "' 가 카테고리로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //전기공학과
        LinearLayout LN5 = (LinearLayout) viewGroup.findViewById(R.id.linearLayout5);
        LN5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment2();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_layout, fragment).commit();

                ((MainActivity) getActivity()).SetNavState(R.id.tab2);
                Fragment2.Search_Url = "https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000348/list.do";
            }
        });
        LN5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NameStr = "전기공학과";
                Alert_Url ="https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000348/list.do";
                Toast.makeText(container.getContext(),"'" + NameStr + "' 가 카테고리로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //정보통신공학과
        LinearLayout LN2 = (LinearLayout) viewGroup.findViewById(R.id.linearLayout2);
        LN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment2();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_layout, fragment).commit();

                ((MainActivity) getActivity()).SetNavState(R.id.tab2);
                Fragment2.Search_Url = "https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000323/list.do";
            }
        });
        LN2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NameStr = "정보통신공학과";
                Alert_Url ="https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000323/list.do";
                Toast.makeText(container.getContext(),"'" + NameStr + "' 가 카테고리로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //전체공지
        LinearLayout LN6 = (LinearLayout) viewGroup.findViewById(R.id.linearLayout6);
        LN6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Fragment2();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_layout, fragment).commit();

                ((MainActivity) getActivity()).SetNavState(R.id.tab2);
                Fragment2.Search_Url = "https://www.hanbat.ac.kr/bbs/BBSMSTR_000000000050/list.do";
            }
        });
        LN6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NameStr = "전체공지";
                Alert_Url ="https://www.hanbat.ac.kr/prog/bbsArticle/BBSMSTR_000000000050/list.do";
                Toast.makeText(container.getContext(),"'" + NameStr + "' 가 카테고리로 설정되었습니다.",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return viewGroup;
    }

}
