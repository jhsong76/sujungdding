package com.example.sujungdding.mypage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.sujungdding.R;

public class MypostView extends LinearLayout {
    TextView mypost_writer, mypost_title, mypost_class, mypost_due, mypost_date;

    public MypostView(Context context){
        super(context);
        init(context);
    }
    public MypostView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.zzim,this, true);

        mypost_writer = findViewById(R.id.zzim_writer);
        mypost_title = findViewById(R.id.zzim_title);
        mypost_class = findViewById(R.id.zzim_class);
        mypost_due = findViewById(R.id.zzim_due);
        mypost_date = findViewById(R.id.zzim_date);
    }

    public void setMypost_writer(String zzim_writer) {
        this.mypost_writer.setText(zzim_writer);
    }
    public void setMypost_title(String zzim_title) { this.mypost_title.setText(zzim_title); }
    public void setMypost_class(String zzim_class) {
        this.mypost_class.setText(zzim_class);
    }
    public void setMypost_due(String zzim_due) {
        this.mypost_due.setText(zzim_due);
    }
    public void setMypost_date(String zzim_date) {
        this.mypost_date.setText(zzim_date);
    }
    public void setMypost_class_bkg(String zzim_class){
        if (zzim_class.equals("한식") || zzim_class.equals("양식") || zzim_class.equals("중식") || zzim_class.equals("일식") || zzim_class.equals("분식") || zzim_class.equals("패스트푸드"))
            this.mypost_class.setBackground(getResources().getDrawable(R.drawable.mypagename));
        else if(zzim_class.equals("문구류") || zzim_class.equals("식품") || zzim_class.equals("생활용품") || zzim_class.equals("인테리어") || zzim_class.equals("디지털"))
            this.mypost_class.setBackground(getResources().getDrawable(R.drawable.chatbackground));
    }
}

