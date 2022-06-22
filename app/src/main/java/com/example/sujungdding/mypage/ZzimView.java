package com.example.sujungdding.mypage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.sujungdding.R;

public class ZzimView extends LinearLayout {
    TextView zzim_writer, zzim_title, zzim_class, zzim_due, zzim_date;

    public ZzimView(Context context){
        super(context);
        init(context);
    }
    public ZzimView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.zzim,this, true);

        zzim_writer = findViewById(R.id.zzim_writer);
        zzim_title = findViewById(R.id.zzim_title);
        zzim_class = findViewById(R.id.zzim_class);
        zzim_due = findViewById(R.id.zzim_due);
        zzim_date = findViewById(R.id.zzim_date);
    }

    public void setZzim_writer(String zzim_writer) {
        this.zzim_writer.setText(zzim_writer);
    }
    public void setZzim_title(String zzim_title) { this.zzim_title.setText(zzim_title); }
    public void setZzim_class(String zzim_class) { this.zzim_class.setText(zzim_class); }
    public void setZzim_due(String zzim_due) {
        this.zzim_due.setText(zzim_due);
    }
    public void setZzim_date(String zzim_date) {
        this.zzim_date.setText(zzim_date);
    }
    public void setZzim_class_bkg(String zzim_class){
        if (zzim_class.equals("한식") || zzim_class.equals("양식") || zzim_class.equals("중식") || zzim_class.equals("일식") || zzim_class.equals("분식") || zzim_class.equals("패스트푸드"))
            this.zzim_class.setBackground(getResources().getDrawable(R.drawable.mypagename));
        else if(zzim_class.equals("문구류") || zzim_class.equals("식품") || zzim_class.equals("생활용품") || zzim_class.equals("인테리어") || zzim_class.equals("디지털"))
            this.zzim_class.setBackground(getResources().getDrawable(R.drawable.chatbackground));
    }
}
