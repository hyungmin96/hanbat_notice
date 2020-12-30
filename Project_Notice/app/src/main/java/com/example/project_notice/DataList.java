package com.example.project_notice;

import android.graphics.Color;

import java.io.Serializable;

public class DataList implements Serializable {

    private String ArticleTitle;
    private String ArticleDate;
    private String FileNAME;
    private String Url;
    private int imageUrl;
    private int Booked;
    private int BackColor;
    private int deleteNum;
    private boolean setChecked;

    public DataList(String ArticleTitle, String ArticleDate, String Url, int imageUrl, int BackColor, int Booked) {
        this.ArticleTitle = ArticleTitle;
        this.ArticleDate = ArticleDate;
        this.Url = Url;
        this.imageUrl = imageUrl;
        this.BackColor = BackColor;
        this.Booked = Booked;
    }

    public DataList(String ArticleTitle, String ArticleDate, String Url, int Booked) {
        this.ArticleTitle = ArticleTitle;
        this.ArticleDate = ArticleDate;
        this.Url = Url;
        this.Booked = Booked;
    }


    public void setTitle(String ArticleTitle) {
        this.ArticleTitle = ArticleTitle;
    }

    public void setDate(String ArticleDate) {
        this.ArticleDate = ArticleDate;
    }

    public void setFileNAME(String FileNAME) {
        this.FileNAME = FileNAME;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setChecked(boolean setChecked) {
        this.setChecked = setChecked;
    }

    public void setColor(int BackColor) { this.BackColor = BackColor;}

    public void setBooked(int Booked) { this.Booked = Booked;}

    public void setdeleteNum(int Booked) { this.deleteNum = deleteNum;}

    public int getdeleteNum() {
        return deleteNum;
    }

    public int getBooked() {
        return Booked;
    }

    public int getColor() { return BackColor; }

    public boolean getChecked() {
        return setChecked;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public String getArticleDate() {
        return ArticleDate;
    }

    public String getFileNAME() {
        return FileNAME;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return Url;
    }

}